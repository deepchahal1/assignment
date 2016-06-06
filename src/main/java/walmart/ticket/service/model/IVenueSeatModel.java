package walmart.ticket.service.model;


import java.util.List;

import walmart.ticket.service.data.state.SeatState;


public interface IVenueSeatModel extends ISeatModel {

	int numSeatsAvailable(int levelId);
	List<SeatState> getBestSeats(final int numSeatsArg, final int minLevelArg, int maxLevelArg) ;
	
}
