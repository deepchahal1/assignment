package walmart.ticket.service.data;

import java.util.Collections;
import java.util.List;

/**
 * 
 * Seat hold details
 * @author deep
 *
 */
public class SeatHold {

	private final SeatHoldKey key;
	private final List<Seat> seatDataList;
	private final HoldResult holdResult;

	/**
	 * 
	 * Constructor.
	 * @param keyArg
	 * @param seatListArgs
	 * @param holdResultArg
	 */
	public SeatHold(final SeatHoldKey keyArg, final List<Seat> seatListArgs, final HoldResult holdResultArg) {
		this.key = keyArg;
		this.holdResult = holdResultArg;
		this.seatDataList = Collections.unmodifiableList(seatListArgs);
	}

	public SeatHoldKey getKey() {
		return key;
	}

	public List<Seat> getSeatList() {
		return seatDataList;
	}

	public HoldResult getHoldResult() {
		return holdResult;
	}

	public static enum HoldResultStatus {
		success, failled
	}

	public static class HoldResult {

		private final HoldResultStatus status;
		private final String reason;

		public HoldResult(final HoldResultStatus statusArg, final String reasonArg) {
			this.reason = reasonArg;
			this.status = statusArg;
		}

		public HoldResultStatus getStatus() {
			return status;
		}

		public String getReason() {
			return reason;
		}
	}
}
