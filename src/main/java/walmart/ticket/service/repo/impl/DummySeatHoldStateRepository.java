package walmart.ticket.service.repo.impl;

import java.util.HashMap;
import java.util.Map;

import walmart.ticket.service.data.SeatHoldKey;
import walmart.ticket.service.data.state.SeatHoldState;
import walmart.ticket.service.repo.IStateRepository;

public class DummySeatHoldStateRepository implements IStateRepository<SeatHoldKey, SeatHoldState>{

	@Override
	public void persist(SeatHoldKey seatHoldState, SeatHoldState value) {
		//not persisting,
		
	}

	@Override
	public Map<SeatHoldKey, SeatHoldState> loadInitialState() {
		//
		//return empty map as we are not persisting 
		return new HashMap<>();
	}

}
