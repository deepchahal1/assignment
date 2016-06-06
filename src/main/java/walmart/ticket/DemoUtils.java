package walmart.ticket;

import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import walmart.ticket.service.TicketService;
import walmart.ticket.service.data.Level;
import walmart.ticket.service.data.SeatHold;
import walmart.ticket.service.data.VenueConfig;
import walmart.ticket.service.data.SeatHold.HoldResultStatus;

/**
 * helper class to run command line option, assert are not used to verify operations.
 * @author deep
 *
 */
public class DemoUtils {
	private static final Logger logger = LoggerFactory.getLogger(DemoUtils.class);
	
	public static void findSeats(final TicketService ticketService, VenueConfig venueConfig, CommandLine line){
		String strMinValueLevel = getValue(line,CmdLineOption.minLevel);
		Optional<Integer> minLevel = strMinValueLevel != null ? Optional.of(Integer.parseInt(strMinValueLevel)) : Optional.empty();
		int numSeatsAvailable = ticketService.numSeatsAvailable(minLevel);
		if(!venueConfig.isValidLevel(minLevel.get())){
			strMinValueLevel= "invalid level: " + strMinValueLevel;
		}
		logger.info("numSeatsAvailable: {}, for level: {}",numSeatsAvailable, strMinValueLevel);
	}
	
	public static SeatHold heldSeats(final TicketService ticketService, VenueConfig venueConfig, CommandLine line){
		String strNumSeats = getValue(line,CmdLineOption.numSeats);
		int numSeats = Integer.parseInt(strNumSeats);
		String strMinValueLevel = getValue(line,CmdLineOption.minLevel);
		String strMaxValueLevel = getValue(line,CmdLineOption.maxLevel);
		Optional<Integer> minLevel = strMinValueLevel != null ? Optional.of(Integer.parseInt(strMinValueLevel)) : Optional.empty();
		Optional<Integer> maxLevel = strMaxValueLevel != null ? Optional.of(Integer.parseInt(strMaxValueLevel)) : Optional.empty();
	    String customerId = getValue(line,CmdLineOption.customerId);
		SeatHold seatHold = ticketService.findAndHoldSeats(numSeats, minLevel, maxLevel, customerId);
		if(seatHold.getHoldResult().getStatus() == HoldResultStatus.success){
		 logger.info("seat hold key: {}, seat count: {}",seatHold.getKey().toString(), seatHold.getSeatList().size());
 
		}else{
			 logger.error("Failed to hold seat, reason: {}",seatHold.getHoldResult().getReason());
		}
		return seatHold;
	}
	
	public  static void holdAndReserveSeats(final TicketService ticketService, VenueConfig venueConfig, CommandLine line){
		SeatHold seatHold = heldSeats(ticketService, venueConfig, line);
		if(seatHold.getHoldResult().getStatus() != HoldResultStatus.success){
			return;
		}
		try{
			String conformationCode = ticketService.reserveSeats(seatHold.getKey().getId(), seatHold.getKey().getCustomerId());
			logger.info("seat reserved, conformation code : {}",conformationCode);
		}catch(RuntimeException ex){
			logger.error("failled to reserve seats: ",ex.getMessage());
		}
		
	}
	/**
	 * Demo
	 * @param ticketService
	 * @param venueConfig
	 * @throws InterruptedException
	 */
	public static void runDemo(final TicketService ticketService, VenueConfig venueConfig) throws InterruptedException{
		//Find total available seats
		logger.info("Running Demo...");
		logger.info("Case 1: Show available seats...");
		Optional<Integer> levelId =   Optional.empty();
		int expectedAvailableSeats = venueConfig.seatsCountAll();
		logger.info("Calling ticketService.numSeatsAvailable(null): {}, expetced: {}",ticketService.numSeatsAvailable(levelId),expectedAvailableSeats);
		for(Level level: venueConfig.getLevels()){
			levelId =   Optional.of(level.getId());
			expectedAvailableSeats = venueConfig.seatsCount(level.getId());
			logger.info("Calling ticketService.numSeatsAvailable({}): {}, expected: {}",level.getId(),ticketService.numSeatsAvailable(levelId),expectedAvailableSeats  );
				
		}
		logger.info("Case 1: End.\n");
		logger.info("Case 2: FindAndHoldSeats 10 seats for level2 ...");
		SeatHold seatHold = ticketService.findAndHoldSeats(10, Optional.of(2), Optional.empty(), "xyz123@walmart.com");
		logger.info("Found and held seat: {}, requested: {}, seatHoldId: {}",seatHold.getSeatList().size(),10,seatHold.getKey().getId());
		levelId =   Optional.of(2);
		expectedAvailableSeats = venueConfig.seatsCount(2) - 10;
		logger.info("Verify left available seat count in level2: {}, expected: {}",  ticketService.numSeatsAvailable(levelId),expectedAvailableSeats);
		String confirmationCode = ticketService.reserveSeats(seatHold.getKey().getId(), seatHold.getKey().getCustomerId());
		logger.info("Reserve held seat for hold id: {}, confirmation code: {}",seatHold.getKey().getId(), confirmationCode);
		logger.info("Verify remaining available seat count in level2: {}, expected: {}",  ticketService.numSeatsAvailable(levelId),expectedAvailableSeats);

		logger.info("Case 2: End.\n");
		
		logger.info("Case 3: FindAndHoldSeats 10 seats for level2 ...");
		SeatHold seatHoldRequest2 = ticketService.findAndHoldSeats(10, Optional.of(2), Optional.empty(), "xyz123@walmart.com");
		logger.info("Found and held seat: {}, requested: {}, seatHoldId: {}",seatHoldRequest2.getSeatList().size(),10,seatHoldRequest2.getKey().getId());
		levelId =   Optional.of(2);
		expectedAvailableSeats = venueConfig.seatsCount(2) - 20;
		logger.info("Verify remaining available seat count in level2: {}, expected: {}",  ticketService.numSeatsAvailable(levelId),expectedAvailableSeats);
		logger.info("Sleeping for 20 seconds so that seat hold get expired.");

		Thread.sleep(20 * 1000);
		try{
			String confirmationCode2 = ticketService.reserveSeats(seatHoldRequest2.getKey().getId(), seatHoldRequest2.getKey().getCustomerId());
		   logger.error("Must not be able to reserve as seat Hold is expired, Reserve held seat for hold id: {}, confirmation code: {}",seatHoldRequest2.getKey().getId(), confirmationCode2);

		}catch(RuntimeException ex){
			expectedAvailableSeats = venueConfig.seatsCount(2) - 10;
		   logger.info("Could not reserve held seat for hold id: {}, it's expected. reason;{}",seatHoldRequest2.getKey(), ex.getMessage());
		   logger.info("Verify remaining available seat count in level2: {}, expected: {}",  ticketService.numSeatsAvailable(levelId),expectedAvailableSeats);
		   logger.info("Case 3: End.\n");
		}

		
		
	}
	public static boolean hasOption(CommandLine line, CmdLineOption option){
		 return line.hasOption(option.name());
	 }
	 public static String getValue(CommandLine line, CmdLineOption option){
		 return line.hasOption(option.name()) ? line.getOptionValue(option.name()) : null ;
		 
	 }
}
