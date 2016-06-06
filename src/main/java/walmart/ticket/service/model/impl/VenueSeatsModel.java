package walmart.ticket.service.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import walmart.ticket.service.data.SeatStatus;
import walmart.ticket.service.data.VenueConfig;
import walmart.ticket.service.data.state.SeatState;
import walmart.ticket.service.model.ILevelSeatModel;
import walmart.ticket.service.model.IVenueSeatModel;


public class VenueSeatsModel implements IVenueSeatModel {

	private static final Logger logger = LoggerFactory.getLogger(VenueSeatsModel.class);
	private final List<ILevelSeatModel> levelSeatStoreList;

	public VenueSeatsModel(final VenueConfig venueArg, final List<ILevelSeatModel> levelSeatStoreListArg) {
		this.levelSeatStoreList = Collections.unmodifiableList(new ArrayList<>(levelSeatStoreListArg));
	}

	public int numSeatsAvailable(int levelId) {
		return levelSeatStoreList.get(levelId - 1).numSeatsAvailable();
	}

	public int numSeatsAvailable() {
		int availableSeats = 0;
		for (ILevelSeatModel levelSeatStore : levelSeatStoreList) {
			availableSeats += levelSeatStore.numSeatsAvailable();
		}
		logger.info("Available seats: {}", availableSeats);
		return availableSeats;
	}

	public List<SeatState> getBestSeats(final int numSeatsArg, final int minLevelArg, int maxLevelArg) {

		int seatsToBeHold = numSeatsArg;
		List<SeatState> seatHoldList = new LinkedList<>();
		for (int levelInex = minLevelArg; levelInex < maxLevelArg; levelInex++) {
			levelSeatStoreList.get(levelInex - 1).getBestSeats(seatsToBeHold, seatHoldList);
			seatsToBeHold = numSeatsArg - seatHoldList.size();
			if (seatsToBeHold <= 0) {
				break;
			}
		}
        if(seatHoldList.size() != numSeatsArg){
        	//
        	// release seat back to model, as not enough seats available as requested
        	for (final SeatState seat : seatHoldList) {
    			seat.release();
    		}
        	return Collections.emptyList();
        }
		for (final SeatState seat : seatHoldList) {
			seat.setStatus(SeatStatus.Held);
		}
		logger.info("Requested seats count:{0}, Held Seats Count:{1}", numSeatsArg, seatsToBeHold);
		return seatHoldList;
	}

}
