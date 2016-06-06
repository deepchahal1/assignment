package walmart.ticket.service.data;

import java.util.Objects;

import com.google.common.base.MoreObjects;


public final class  SeatHoldKey {

	private final int id;
	private final String customerId;

	public SeatHoldKey(final int idArg, final String cusomerIdArg) {
		this.id = idArg;
		this.customerId = cusomerIdArg;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		SeatHoldKey seatHoldKey = (SeatHoldKey) obj;
		return Objects.equals(this.id, seatHoldKey.id) && Objects.equals(customerId, seatHoldKey.customerId);

	}

	@Override
	public int hashCode() {
		return Objects.hash(id, customerId);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
		           		  .add("seatHoldId", id)
		           		  .add("customerId", customerId)
		           		  .toString();
	}
	
	public int getId() {
		return id;
	}

	public String getCustomerId() {
		return customerId;
	}

}
