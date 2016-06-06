package walmart.ticket.service;

import walmart.ticket.service.data.state.SeatState;

/**
 * used to call back to release seat back to level mdoel.
 * @author deep
 *
 */
public interface ISeatHoldExpired {
    /**
     * call on seat hold expired.
     * @param seat
     */
	void seatHoldExpired(final SeatState seat);
}
