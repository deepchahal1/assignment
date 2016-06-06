package walmart.ticket.service.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public final class VenueConfig {
	
    private final static int START_LEVEL_INDEX = 1;
	private final List<Level> levels;	
	
	public VenueConfig(final List<Level> levelsArg ){
		final List<Level> levelList = new ArrayList<>(levelsArg);
		this.levels = Collections.unmodifiableList(levelList);
				 
	}
	public int seatsCount(int levelId){
		return levels.get(levelId - START_LEVEL_INDEX).seatsCount();
	}
	
	public int seatsCountAll(){
		int seatsCountAll = 0;
		for(Level level: levels){
			seatsCountAll += level.seatsCount();
		}
		return seatsCountAll;
	}

	public List<Level> getLevels() {
		return levels;
	}
	
	public boolean isValidLevel(int levelIndex){
		return (levelIndex >= START_LEVEL_INDEX && levelIndex  <= levels.size());
	}
	
	public int getMinLevel(final Optional<Integer> minLevelArg){
		int minLevel = (minLevelArg != null && minLevelArg.isPresent()) ? minLevelArg.get() : 1;
		return minLevel;
	}
	
	public int getMaxLevel(final Optional<Integer> maxLevelArg){
		int minLevel = ( maxLevelArg != null && maxLevelArg.isPresent()) ? maxLevelArg.get() : levels.size();
		return minLevel;
	}
}
