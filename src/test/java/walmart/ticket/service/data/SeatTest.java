package walmart.ticket.service.data;

import static org.junit.Assert.*;

import org.junit.Test;



public class SeatTest {
	

	@Test
	public void testAllGetter() {
		Level level =  new Level(1, "test", 100.00, 20, 50);
		Seat seat = new Seat(level, 20, 1, 5);
		assertTrue(level == seat.getLevel());
		assertTrue(20 == seat.getSeatNumber());
		assertTrue(1 == seat.getRowNumber());
		assertTrue(5 == seat.getSeatPositionRank());
		
	}
	@Test
	public void testCompareToLess() {
		Level level =  new Level(1, "test", 100.00, 20, 50);
		Seat seatNumOne = new Seat(level, 20, 1, 5);
		Seat seatNumtwo = new Seat(level, 19, 1, 6);
		assertTrue(seatNumOne.compareTo(seatNumtwo) == -1);
		
	}
	@Test
	public void testCompareToEqual() {
		Level level =  new Level(1, "test", 100.00, 20, 50);
		Seat seatNumOne = new Seat(level, 20, 1, 5);
		Seat seatNumtwo = new Seat(level, 20, 1, 5);
		assertTrue(seatNumOne.compareTo(seatNumtwo) == 0);
		
	}
	@Test
	public void testCompareToGreater() {
		Level level =  new Level(1, "test", 100.00, 20, 50);
		Seat seatNumOne = new Seat(level, 20, 1, 5);
		Seat seatNumtwo = new Seat(level, 21, 1, 4);
		assertTrue(seatNumOne.compareTo(seatNumtwo) == 1);
		
	}

}
