package walmart.ticket.service.impl;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import walmart.ticket.service.TicketService;
import walmart.ticket.service.data.SeatHold;
import walmart.ticket.service.data.SeatHold.HoldResult;
import walmart.ticket.service.data.SeatHold.HoldResultStatus;
import walmart.ticket.service.data.SeatHoldKey;
import walmart.ticket.service.data.VenueConfig;
import walmart.ticket.service.data.state.ReserveCmdResponse;
import walmart.ticket.service.data.state.SeatState;
import walmart.ticket.service.data.state.ReserveCmdResponse.ReserveCmdSatus;
import walmart.ticket.service.model.impl.VenueSeatHoldModel;
import walmart.ticket.service.model.impl.VenueSeatsModel;

/**
 * Ticket service implementation.
 * @author deep
 *
 */

public class TicketServiceImpl implements TicketService{
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

	private final VenueSeatsModel venueSeatMemoryStore;
	private final VenueSeatHoldModel venueSeatHoldMemoryStore;
	private final VenueConfig venueConfig;
	private final EmailValidator emailValidator =  EmailValidator.getInstance();
	
	/**
	 * Constructor.
	 * @param venueSeatMemoryStoreArg
	 * @param venueSeatHoldMemoryStoreArg
	 * @param venueConfigArg
	 */
	public TicketServiceImpl(final VenueSeatsModel venueSeatMemoryStoreArg,
			final VenueSeatHoldModel venueSeatHoldMemoryStoreArg,
			final VenueConfig venueConfigArg){
		this.venueSeatMemoryStore = venueSeatMemoryStoreArg;
		this.venueSeatHoldMemoryStore = venueSeatHoldMemoryStoreArg;
		this.venueConfig = venueConfigArg;
	}
	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		if(venueLevel != null && venueLevel.isPresent() &&  venueConfig.isValidLevel(venueLevel.get())){
			return venueSeatMemoryStore.numSeatsAvailable(venueLevel.get());
		}else{
			return venueSeatMemoryStore.numSeatsAvailable();
		}
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeatsArg, Optional<Integer> minLevelArg, Optional<Integer> maxLevelArg,
			String customerEmail) {
		if(!emailValidator.isValid(customerEmail)){
			String errorMessage = MessageFormat.format("Invalid email id: {0}", customerEmail);
			logger.error(errorMessage);
			throw new RuntimeException(errorMessage );
		}
		int minLevelId = venueConfig.getMinLevel(minLevelArg);
		int maxLevelId = venueConfig.getMaxLevel(maxLevelArg);
		List<SeatState> heldSeatList = venueSeatMemoryStore.getBestSeats(numSeatsArg, minLevelId, maxLevelId);
		if(heldSeatList.size() != numSeatsArg){
			// could not find requested seats.
			SeatHoldKey key = new SeatHoldKey(-1, customerEmail);
			return new SeatHold(key,Collections.emptyList(), new HoldResult(HoldResultStatus.failled, "Not enough seats availables"));
		}
		SeatHold seatHold =  venueSeatHoldMemoryStore.holdSeats(customerEmail, heldSeatList);
		return seatHold;
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		ReserveCmdResponse response = venueSeatHoldMemoryStore.reserveSeats(new SeatHoldKey(seatHoldId, customerEmail));
		if(response.status == ReserveCmdSatus.Reserved){
			logger.info("Seat reserved for seat hold id:{0}, customerEmail:{1}",seatHoldId,customerEmail);
			return response.getConfirmationCode();
		}
		throw new RuntimeException(response.getReason());
	}

}
