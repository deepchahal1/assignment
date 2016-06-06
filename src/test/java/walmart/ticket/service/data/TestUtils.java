package walmart.ticket.service.data;

import java.util.Arrays;
import java.util.List;

public class TestUtils {

	public static VenueConfig getMockVenueConfig(){
		Level level1 = new Level(1, "test1", 100.00, 2, 10);
		Level level2 = new Level(1, "test2", 75.00, 3, 20);
		Level level3 = new Level(1, "test3", 50.00, 4, 30);
		Level level4 = new Level(1, "test4", 40.00, 5, 40);
		List<Level> levelList = Arrays.asList(level1,level2,level3,level4);
		return   new VenueConfig(levelList);
	}
}
