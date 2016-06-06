package walmart.ticket.service.data;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VenueConfigTest {

	private static VenueConfig venueConfig;
	@Before
	public void setUp() throws Exception {
		Level level1 = new Level(1, "test1", 100.00, 2, 10);
		Level level2 = new Level(1, "test2", 75.00, 3, 20);
		Level level3 = new Level(1, "test3", 50.00, 4, 30);
		Level level4 = new Level(1, "test4", 40.00, 5, 40);
		List<Level> levelList = Arrays.asList(level1,level2,level3,level4);
		venueConfig =  new VenueConfig(levelList);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidLevel() {
		assertTrue(venueConfig.isValidLevel(1));
		assertTrue(venueConfig.isValidLevel(2));
		assertTrue(venueConfig.isValidLevel(3));
		assertTrue(venueConfig.isValidLevel(4));
	}
	@Test
	public void testInvalidLevel() {
		assertFalse(venueConfig.isValidLevel(0));
		assertFalse(venueConfig.isValidLevel(5));
		assertFalse(venueConfig.isValidLevel(-1));
		
	}
	@Test
	public void testMinLevel() {
		assertTrue(venueConfig.getMinLevel(null) == 1);
		assertTrue(venueConfig.getMinLevel(Optional.of(2)) == 2);
	}
	@Test
	public void testMaxLevel() {
		assertTrue(venueConfig.getMaxLevel(null) == 4);
		assertTrue(venueConfig.getMaxLevel(Optional.of(2)) == 2);
	}
   
	@Test
	public void testAllSeatCount() {
		assertTrue(venueConfig.seatsCountAll() == 400);
		
	}
	@Test
	public void testSeatCount() {
		assertTrue(venueConfig.seatsCount(1) == 20);
		assertTrue(venueConfig.seatsCount(2) == 60);
		
	}
}
