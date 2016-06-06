package walmart.ticket.service.data;



import org.junit.After;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;




public class LevelTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAllGetter() {
		Level level =  new Level(1, "Orchestra", 100.00, 25, 50);
		assertTrue(1 == level.getId());
		assertTrue("Orchestra".equals(level.getName()));
		assertTrue(Double.compare(100.00,level.getPrice()) == 0);
		assertTrue(25 == level.getNoOfRows());
		assertTrue(1250 == level.seatsCount());
	}
	
	@Test
	public void testCompareToEqual() {
		Level levelIntanace1 =  new Level(1, "Orchestra", 100.00, 25, 50);
		Level levelIntanace2 =  new Level(1, "Orchestra", 100.00, 25, 50);
		assertTrue(levelIntanace1.compareTo(levelIntanace2) == 0);
		
	}
	@Test
	public void testCompareToLess() {
		Level levelIntanace1 =  new Level(1, "Orchestra", 100.00, 25, 50);
		Level levelIntanace2 =  new Level(2, "Main", 75.00, 20, 100);
		assertTrue(levelIntanace1.compareTo(levelIntanace2) == -1);
		
	}
	@Test
	public void testCompareToGreater() {
		Level levelIntanace1 =  new Level(1, "Orchestra", 100.00, 25, 50);
		Level levelIntanace2 =  new Level(2, "Main", 75.00, 20, 100);
		assertTrue(levelIntanace2.compareTo(levelIntanace1) == 1);
		
	}

}
