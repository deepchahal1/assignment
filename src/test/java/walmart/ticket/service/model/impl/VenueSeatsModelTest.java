package walmart.ticket.service.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import walmart.ticket.service.data.TestUtils;
import walmart.ticket.service.data.VenueConfig;
import walmart.ticket.service.data.state.SeatState;
import walmart.ticket.service.model.ILevelSeatModel;

public class VenueSeatsModelTest {

	private static VenueSeatsModel venueSeatsModel;
	private static VenueConfig venueConfig = TestUtils.getMockVenueConfig();
	@Before
	public void setUp() throws Exception {
		ILevelSeatModel levelSeatModel1 = Mockito.mock(ILevelSeatModel.class);
		ILevelSeatModel levelSeatModel2 = Mockito.mock(ILevelSeatModel.class);
		ILevelSeatModel levelSeatModel3 = Mockito.mock(ILevelSeatModel.class);
		ILevelSeatModel levelSeatModel4 = Mockito.mock(ILevelSeatModel.class);
        List<ILevelSeatModel> seatModelList = Arrays.asList(levelSeatModel1, levelSeatModel2, levelSeatModel3, levelSeatModel4);
		Mockito.when(levelSeatModel1.numSeatsAvailable()).thenReturn(10);
		Mockito.when(levelSeatModel2.numSeatsAvailable()).thenReturn(20);
		Mockito.when(levelSeatModel3.numSeatsAvailable()).thenReturn(30);
		Mockito.when(levelSeatModel4.numSeatsAvailable()).thenReturn(40);
	     venueSeatsModel =  new VenueSeatsModel(venueConfig, seatModelList);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testnumSeatsAvailable() {
		assertTrue( 100 == venueSeatsModel.numSeatsAvailable());
	}
	
	
	@Test
	public void testSeatsAvailable() {
		assertTrue( 10 == venueSeatsModel.numSeatsAvailable(1));
		assertTrue( 20 == venueSeatsModel.numSeatsAvailable(2));
		assertTrue( 30 == venueSeatsModel.numSeatsAvailable(3));
		assertTrue( 40 == venueSeatsModel.numSeatsAvailable(4));

	}

}
