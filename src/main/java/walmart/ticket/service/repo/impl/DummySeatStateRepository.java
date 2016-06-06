package walmart.ticket.service.repo.impl;

import java.util.HashMap;
import java.util.Map;

import walmart.ticket.service.data.Seat;
import walmart.ticket.service.data.state.SeatState;
import walmart.ticket.service.repo.IStateRepository;

/**
 * This is dummy SeatState persister.
 * @author deep
 *
 */
public class DummySeatStateRepository implements IStateRepository<Seat, SeatState>{

	@Override
	public void persist(Seat seatHoldState, SeatState value) {
		//do nothing, will not persist.
		
	}

	@Override
	public Map<Seat, SeatState> loadInitialState() {
		//
		// return empty hashmap as we are not persisting states.
		//
		return new HashMap<>();
	}

}
