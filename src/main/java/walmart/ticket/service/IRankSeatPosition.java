package walmart.ticket.service;

import walmart.ticket.service.data.Level;

/**
 * interface to give a best position rank to seat.
 * @author deep
 *
 */
public interface IRankSeatPosition {

	int rankSeatPosition(final int seatNumber, final Level levelArg);
}
