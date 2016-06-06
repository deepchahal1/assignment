package walmart.ticket.service.data.state;

/**
 * this class encapsulate the reserve seat operation result.
 * @author deep
 *
 */
public class ReserveCmdResponse {

	public final ReserveCmdSatus status;
	private final String reason;
	private final String confirmationCode;
	
	/**
	 * Constructor.
	 * @param statusArg
	 * 					ReserveCmdSatus
	 * @param reasonArg
	 *                  String
	 * @param confirmationCodeArg
	 *                  String
	 */
	public ReserveCmdResponse(final ReserveCmdSatus statusArg, final String reasonArg, final String confirmationCodeArg){
		this.status = statusArg;
		this.reason = reasonArg;
		this.confirmationCode = confirmationCodeArg;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public ReserveCmdSatus getStatus() {
		return status;
	}

	public String getReason() {
		return reason;
	}
	
	public static enum ReserveCmdSatus {

		Reserved,
		InValidSeatHoldKey,
		InvalidState
	}

}
