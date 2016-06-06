package walmart.ticket.service.model.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import walmart.ticket.service.IConfirmmationCodeGenerator;
import walmart.ticket.service.data.SeatHold;
import walmart.ticket.service.data.SeatHoldKey;
import walmart.ticket.service.data.state.ReserveCmdResponse;
import walmart.ticket.service.data.state.SeatHoldState;
import walmart.ticket.service.data.state.SeatState;
import walmart.ticket.service.data.state.ReserveCmdResponse.ReserveCmdSatus;
import walmart.ticket.service.repo.IStateRepository;


/**
 * This class encapsulate all hold seat seats, background ScheduledThreadPoolExecutor thread keep checking
 * SeatHold status, any expired SeatHold release back to LevelSeatModel.
 * 
 * @author deep
 *
 */
public  class VenueSeatHoldModel {

	private static final Logger logger = LoggerFactory.getLogger(VenueSeatHoldModel.class);

    private final AtomicInteger seatHoldId = new AtomicInteger(1);
    private final ConcurrentHashMap<SeatHoldKey, SeatHoldState> seatHoldMap =  new ConcurrentHashMap<>();
	private final ConcurrentLinkedQueue<SeatHoldState> seatHoldQueue = new ConcurrentLinkedQueue<>();
	private final IConfirmmationCodeGenerator confirmmationCodeGenerator;
	private final IStateRepository<SeatHoldKey, SeatHoldState> seatHoldRepo;
	private final ScheduledThreadPoolExecutor scheduledExecutor;
	private final int maxHoldTimeInSecs;
	
	/**
	 * 
	 * @param confirmmationCodeGeneratorArg 
	 *             confirmation code generator.
	 * @param scheduledExecutorArg
	 *         scheduled thread pool for releasing Expired seats back to VenueStore.
	 * @param maxHoldTimeInSecsArg
	 *         max hold time before seat hold expired.
	 */
	public VenueSeatHoldModel(final IConfirmmationCodeGenerator confirmmationCodeGeneratorArg, final IStateRepository<SeatHoldKey, SeatHoldState> seatHoldRepoArg, final ScheduledThreadPoolExecutor scheduledExecutorArg,
									final int maxHoldTimeInSecsArg){
		this.confirmmationCodeGenerator = confirmmationCodeGeneratorArg;
		this.seatHoldRepo = seatHoldRepoArg;
		this.scheduledExecutor = scheduledExecutorArg;
		this.maxHoldTimeInSecs = maxHoldTimeInSecsArg;
		this.scheduledExecutor.scheduleAtFixedRate(new ReleaseExpiredSeatHoldTask(this.seatHoldQueue,this.maxHoldTimeInSecs), this.maxHoldTimeInSecs, 1, TimeUnit.SECONDS);
		loadInitialSeatHoldState();
	}
	/**
	 * load initial state.
	 */
	private  void loadInitialSeatHoldState(){
		final Map<SeatHoldKey, SeatHoldState> seatHoldStateMap = seatHoldRepo.loadInitialState();
		for(SeatHoldState seatHold: seatHoldStateMap.values() ){
			seatHoldMap.put(seatHold.getKey(), seatHold);
			if(seatHold.isHeld()){
				seatHoldQueue.add(seatHold);
			}
		}
	}
	
	/**
	 * Hold seats for a customer.
	 * @param customerIdArg
	 * @param heldSeatListArg
	 * @return
	 */
	public SeatHold holdSeats(final String customerIdArg, List<SeatState> heldSeatListArg){
		final SeatHoldKey seatHoldKey = new SeatHoldKey(seatHoldId.getAndIncrement(), customerIdArg);
		final SeatHoldState seatHold = new SeatHoldState(seatHoldKey, heldSeatListArg);
		this.seatHoldMap.put(seatHoldKey, seatHold);
		this.seatHoldQueue.add(seatHold);
	    return seatHold.getSeatHold();
	}
	
	/**
	 * reserver seat against a hold id.
	 * @param seatHoldKeyArg
	 * @return
	 */
	public ReserveCmdResponse reserveSeats(final SeatHoldKey seatHoldKeyArg){
		final SeatHoldState seatHold = seatHoldMap.get(seatHoldKeyArg);
		String reason;
			if(seatHold != null){
				if(!seatHold.isExpired(this.maxHoldTimeInSecs)){
				synchronized (seatHold) {
					if(seatHold.isHeld()){
						seatHold.reserveSeats();
						final String confirmCode = this.confirmmationCodeGenerator.generateCode();
						seatHold.setConfirmationCode(confirmCode);
						return new ReserveCmdResponse(ReserveCmdSatus.Reserved,"", confirmCode);
					}else if(seatHold.isReserved()){
						return new ReserveCmdResponse(ReserveCmdSatus.Reserved,"Already reserved", seatHold.getConfirmationCode());
					}
				}
				}else{
					reason = MessageFormat.format("Can not reserved seat for SeatHoldKey: {0}, Seat Hold status:{1}", seatHoldKeyArg.toString(),seatHold.getStatus());
					return new ReserveCmdResponse(ReserveCmdSatus.InvalidState, reason, seatHold.getConfirmationCode());
				}
		}
		reason = MessageFormat.format("Seat hold not found for SeatHoldKey: {0}", seatHoldKeyArg.toString());
		logger.error(reason);
		return new ReserveCmdResponse(ReserveCmdSatus.InValidSeatHoldKey, reason, "");
		
	}
	/**
	 * this task will be executed in by scheduler at fix frequency, this will release expired seat holds
	 * to back to level model.
	 * @author deep
	 *
	 */
	public  static class ReleaseExpiredSeatHoldTask implements  Runnable {
		private static final Logger logger = LoggerFactory.getLogger(ReleaseExpiredSeatHoldTask.class);
		private final ConcurrentLinkedQueue<SeatHoldState> seatHoldQueue;
        private final int held_Max_Seconds;
		public ReleaseExpiredSeatHoldTask(final ConcurrentLinkedQueue<SeatHoldState> seatHoldQueueArg,final int held_Max_SecondsArg ){
			this.seatHoldQueue = seatHoldQueueArg;
			this.held_Max_Seconds = held_Max_SecondsArg;
		}
		@Override
		public void run() {
			
			while(true){
					SeatHoldState seatHold = this.seatHoldQueue.peek();
					if(seatHold == null) break; // will check in next scheduled run.
					
					switch (seatHold.getStatus()){
					case  Reserved:
					case Expired:
						this.seatHoldQueue.remove(seatHold);
						break;
					case Held:
						if(seatHold.getSeatHoldTime().plusSeconds(held_Max_Seconds).isBefore(LocalDateTime.now())){
							synchronized (seatHold) {
								if(seatHold.isHeld()){ //make sure seat Hold status is still held.
									seatHold.seatHoldExpired();
									logger.info("Expired seat hold key: {} ", seatHold.getSeatHold().getKey().toString());
									this.seatHoldQueue.remove(seatHold);
								}
							}
						
					}
		
			}
		}
		}
	}
		
	
}
