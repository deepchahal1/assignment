package walmart.ticket.service.data.state;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import walmart.ticket.service.data.Seat;
import walmart.ticket.service.data.SeatHold;
import walmart.ticket.service.data.SeatHoldKey;

public class SeatHoldStateTest {

	private static SeatHoldState seatHoldState;
	private static SeatState seatStateMock;
	@Before
	public void setUp() throws Exception {
		SeatHoldKey key = new SeatHoldKey(1, "xyz@abc.com");
		seatStateMock = Mockito.mock(SeatState.class);
		List<SeatState> seatStateList = Arrays.asList(seatStateMock);
		seatHoldState =  new SeatHoldState(key, seatStateList);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSeatHold() {
		Seat seatMock = Mockito.mock(Seat.class);
	    Mockito.when(seatStateMock.getSeat()).thenReturn(seatMock);
		SeatHold seatHold = seatHoldState.getSeatHold();
		SeatHoldKey key = new SeatHoldKey(1, "xyz@abc.com");
		assertTrue(key.equals(seatHold.getKey()));
		assertTrue(seatHold.getSeatList().size() == 1);
		assertTrue(seatHold.getSeatList().get(0) == seatMock);
	}

}
