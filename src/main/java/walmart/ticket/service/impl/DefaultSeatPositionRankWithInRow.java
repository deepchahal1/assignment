package walmart.ticket.service.impl;

import walmart.ticket.service.IRankSeatPosition;
import walmart.ticket.service.data.Level;


/**
 * this class give best position ranking to a seat with in a row.
 * With in a Row, middle seat have better rank than both end of row.
 * smaller the rank number, better the rank. 
 * @author deep
 *
 */
public class DefaultSeatPositionRankWithInRow implements IRankSeatPosition {

	@Override
	public int rankSeatPosition(final int seatNumber, final Level levelArg) {
		int NotOfOddOrEven = levelArg.getSeatPerRow() % 2 == 0 ? 1 : 0;
		int midOfRow = levelArg.getSeatPerRow()/2;
		int rank;
		if(seatNumber <= midOfRow){
			rank = midOfRow - seatNumber + NotOfOddOrEven;
		}else{
			rank = seatNumber - midOfRow;
		}
		return rank;
	}

}
