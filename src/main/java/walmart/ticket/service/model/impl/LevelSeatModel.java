package walmart.ticket.service.model.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import walmart.ticket.service.IRankSeatPosition;
import walmart.ticket.service.ISeatHoldExpired;
import walmart.ticket.service.data.Level;
import walmart.ticket.service.data.Seat;
import walmart.ticket.service.data.SeatStatus;
import walmart.ticket.service.data.state.SeatState;
import walmart.ticket.service.model.ILevelSeatModel;
import walmart.ticket.service.repo.IStateRepository;

/**
 * This class encapsulate all available seats, seat are stored in priority queue and are in sorted form, best seat 
 * are at head of queue.
 * @author deep
 *
 */
public class LevelSeatModel implements ILevelSeatModel{

	private final Level level;
	private final Set<Seat> seatsSet; // to check if release seat really belong to this level
	// best seats are available at head of queue, it will be efficient to pick already sorted seats
	private final PriorityBlockingQueue<Seat> availableSeatsQueue ;
	// provide best position rank to a seat.
	private final IRankSeatPosition rankSeatPosition;
	private final IStateRepository<Seat, SeatState> seatStateRepo;
	private final ISeatHoldExpired seatHoldNotifier = new SeatHoldNotifer();
	
	
	/**
	 * Constructor.
	 * @param levelArg
	 * @param rankSeatPositionArg
	 * @param seatStateRepoArg
	 */
	public LevelSeatModel(final Level levelArg, final IRankSeatPosition rankSeatPositionArg, final IStateRepository<Seat,SeatState> seatStateRepoArg){
		this.level = levelArg;
		this.rankSeatPosition = rankSeatPositionArg;
		this.seatStateRepo = seatStateRepoArg;
		int totalSeats = this.level.getNoOfRows() * this.level.getSeatPerRow();
		this.availableSeatsQueue = new PriorityBlockingQueue<>();
		this.seatsSet = initializeSeats(totalSeats);
		
		
	}
	/*
	 * load initial state of seat.
	 */
	private Set<Seat> initializeSeats(int totalSeats){
		final Set<Seat> allSeats = new HashSet<>(totalSeats);
		final Map<Seat, SeatState> seatStateMap = this.seatStateRepo.loadInitialState();
		for(int rowIndex = 1; rowIndex <= this.level.getNoOfRows(); rowIndex++){
			for(int seatNumber = 1; seatNumber <= this.level.getSeatPerRow(); seatNumber++){
				final int positionRankWithInRow = rankSeatPosition.rankSeatPosition(seatNumber, this.level);
				final Seat seat = new  Seat(this.level,seatNumber,rowIndex,positionRankWithInRow);
				final SeatState seatState = seatStateMap.get(seat);
				if(seatState == null || (seatState != null  && seatState.getStatus() == SeatStatus.Available)){
					availableSeatsQueue.add(seat);
				}
				allSeats.add(seat);
			}
		}
		return Collections.unmodifiableSet(allSeats);
	}
			
	public int numSeatsAvailable(){
		return this.availableSeatsQueue.size();
	}
	
	public void getBestSeats(final int maxSeatsArg, final List<SeatState> seatListArg){
		List<Seat> foundList = new ArrayList<>(maxSeatsArg);
		availableSeatsQueue.drainTo(foundList,  maxSeatsArg);
		for(Seat seat:foundList ){
			final SeatState seatState = new SeatState(seat, seatHoldNotifier);
			seatState.setStatus(SeatStatus.Held);
			seatListArg.add(seatState);
			seatStateRepo.persist(seat, seatState);
		}
		
	}
   /**
    * handle call for expired seat hold and add them back to priority queue.
    * @author deep
    *
    */
	private class SeatHoldNotifer implements ISeatHoldExpired{
		@Override
		public void seatHoldExpired(final SeatState seat) {
			if(seatsSet.contains(seat.getSeat())){
				availableSeatsQueue.put(seat.getSeat());
			}else{
				final String message = MessageFormat.format("Seat was not created by VenueLevelSeatStore: seat:{0}", seat.toString());
				throw new RuntimeException(message);
			}
				
	} 
	}

	
}
