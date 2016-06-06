package walmart.ticket.service.data;

import java.util.Objects;

import com.google.common.collect.ComparisonChain;


/**
 * 
 * Encapsulate a Seat detail.
 * @author deep
 *
 */
public class Seat implements Comparable<Seat> {

	private final Level level;
	private final int rowNumber;
	private final int seatNumber;
	private final int seatPositionRank;
	
	/**
	 * Constructor.
	 * @param levelArg
	 * @param seatNumberArg
	 * @param rowNumberArg
	 * @param positionRankWithInRow
	 */
	public Seat(final Level levelArg, final int seatNumberArg, final int rowNumberArg, final int positionRankWithInRow){
		 this.level = levelArg;
		 this.seatNumber = seatNumberArg;
		 this.rowNumber = rowNumberArg;
		 this.seatPositionRank = positionRankWithInRow;
	
	}
	public Level getLevel() {
		return level;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public int getSeatPositionRank() {
		return seatPositionRank;
	}
	@Override
	public int hashCode() {
		return Objects.hash(level, rowNumber, seatNumber);
	}
	@Override
	public int compareTo(Seat seatArg) {
		//
		//this function front seat will be in front in ascending order.
		//
		return ComparisonChain.start()
		               .compare(this.getLevel().getId(), seatArg.getLevel().getId())
		               .compare(this.getRowNumber(), seatArg.getRowNumber())
		               .compare(this.getSeatPositionRank(), seatArg.getSeatPositionRank())
		               .result();
	}

	
}
