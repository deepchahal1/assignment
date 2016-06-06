package walmart.ticket.service.model.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import walmart.ticket.service.IRankSeatPosition;
import walmart.ticket.service.data.Level;
import walmart.ticket.service.data.Seat;
import walmart.ticket.service.data.state.SeatState;
import walmart.ticket.service.repo.IStateRepository;

public class LevelSeatModelTest {

	private static LevelSeatModel levelSeatModel; 
	private static Level levelMock;
	private static IStateRepository<Seat, SeatState> stateRepoMock;
	@Before
	public void setUp() throws Exception {
		IRankSeatPosition rankPositionMock = Mockito.mock(IRankSeatPosition.class);
		stateRepoMock = Mockito.mock(IStateRepository.class);
	    levelMock = Mockito.mock(Level.class);			
	    Mockito.when(levelMock.getNoOfRows()).thenReturn(2);
		Mockito.when(levelMock.getSeatPerRow()).thenReturn(10);
		Mockito.when(stateRepoMock.loadInitialState()).thenReturn(new HashMap<>());
		levelSeatModel = new LevelSeatModel(levelMock, rankPositionMock, stateRepoMock);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNumSeatsAvailable() {
		assertTrue( 20 == levelSeatModel.numSeatsAvailable());
		
	}
	
	@Test
	public void testBestSeatsAvailable() {
		List<SeatState> seatStateList = new ArrayList<>();
		levelSeatModel.getBestSeats(5, seatStateList);
		assertTrue( 5 == seatStateList.size());
		
	}

}
