package walmart.ticket.service.functional.cases;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import walmart.ticket.DemoUtils;
import walmart.ticket.service.TicketService;
import walmart.ticket.service.data.Level;
import walmart.ticket.service.data.SeatHold;
import walmart.ticket.service.data.VenueConfig;

public class FunctionalTest {
	private static TicketService ticketService;
	private static  VenueConfig venueConfig;

	@Before
	public void setUp() throws Exception {
		ApplicationContext context =  new ClassPathXmlApplicationContext("/walmart/ticket/config/ticketService.xml");
	    ticketService=(TicketService)context.getBean("ticketService");
	    venueConfig = (VenueConfig )context.getBean("venueConfig");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNumSeatsAvailable() throws InterruptedException {
		
		Optional<Integer> levelId =   Optional.empty();
		int expectedAvailableSeats = venueConfig.seatsCountAll();
		assertTrue(expectedAvailableSeats == ticketService.numSeatsAvailable(levelId));
		for(Level level: venueConfig.getLevels()){
			levelId =   Optional.of(level.getId());
			expectedAvailableSeats = venueConfig.seatsCount(level.getId());
			assertTrue(expectedAvailableSeats == ticketService.numSeatsAvailable(levelId));
				
		}
	}
	@Test
	public void testReserveSeat() throws InterruptedException {
		
		Optional<Integer> minlevelId =   Optional.of(1);;
		SeatHold seatHold = ticketService.findAndHoldSeats(10, minlevelId, Optional.empty(), "xva@abc.com");
		assertTrue(10 == seatHold.getSeatList().size());
		int expectedRemaingAvailableSeats = venueConfig.seatsCount(minlevelId.get()) - 10;

		assertTrue(expectedRemaingAvailableSeats == ticketService.numSeatsAvailable(minlevelId));
		
		String confirmationCode = ticketService.reserveSeats(seatHold.getKey().getId(), seatHold.getKey().getCustomerId());
		
		assertTrue(confirmationCode.isEmpty() == false);
	
		}
	@Test
	public void testExpireSeatHold() throws InterruptedException {
		
		Optional<Integer> minlevelId =   Optional.of(1);;
		SeatHold seatHold = ticketService.findAndHoldSeats(10, minlevelId, Optional.empty(), "xva@abc.com");
		assertTrue(10 == seatHold.getSeatList().size());
		int expectedRemaingAvailableSeats = venueConfig.seatsCount(minlevelId.get()) - 10;

		assertTrue(expectedRemaingAvailableSeats == ticketService.numSeatsAvailable(minlevelId));
		
		Thread.sleep(15 * 1000);
		//
		//Seat hold shouldbe expired till now, and hold seat should be release back to venue level Model
		
		expectedRemaingAvailableSeats = venueConfig.seatsCount(minlevelId.get());

		assertTrue(expectedRemaingAvailableSeats == ticketService.numSeatsAvailable(minlevelId));
		
	
		}
	

}
