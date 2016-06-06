package walmart.ticket.service.model;

import java.util.List;

import walmart.ticket.service.data.state.SeatState;

public interface ILevelSeatModel extends ISeatModel {

	public void getBestSeats(final int maxSeatsArg, final List<SeatState> seatListArg);
}
