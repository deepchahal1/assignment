package walmart.ticket.service.data.state;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import walmart.ticket.service.data.Seat;
import walmart.ticket.service.data.SeatHold;
import walmart.ticket.service.data.SeatHoldKey;
import walmart.ticket.service.data.SeatHoldStatus;
import walmart.ticket.service.data.SeatStatus;
import walmart.ticket.service.data.SeatHold.HoldResult;
import walmart.ticket.service.data.SeatHold.HoldResultStatus;

public  class SeatHoldState {

	private final SeatHoldKey key;
	private final List<SeatState> seats;
	private final LocalDateTime seatHoldTime;
	private volatile String confirmationCode;
	private SeatHoldStatus status = SeatHoldStatus.Held;

	public SeatHoldState(final SeatHoldKey keyArg, final List<SeatState> seatsArg) {
		this.key = keyArg;
		this.seats = Collections.unmodifiableList(seatsArg);
		this.seatHoldTime = LocalDateTime.now();
	}

	public SeatHold getSeatHold() {
		List<Seat> heldSeatsList = new ArrayList<>(seats.size());
		for (SeatState seat : seats) {
			heldSeatsList.add(seat.getSeat());
		}
		return new SeatHold(key, heldSeatsList, new HoldResult(HoldResultStatus.success, ""));
	}

	public SeatHoldKey getKey() {
		return this.key;
	}

	public int getId() {
		return key.getId();
	}

	public String getCustomerId() {
		return key.getCustomerId();
	}

	public List<SeatState> getSeats() {
		return seats;
	}

	public int getSeatsCount() {
		return seats.size();
	}

	public LocalDateTime getSeatHoldTime() {
		return seatHoldTime;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public synchronized void reserveSeats() {
		setStatus(SeatHoldStatus.Reserved);
		for (SeatState seat : seats) {
			seat.setStatus(SeatStatus.Reserved);
		}
	}

	public synchronized void seatHoldExpired() {
		setStatus(SeatHoldStatus.Expired);
		for (SeatState seat : seats) {
			// release seat back to Level store.
			seat.release();
		}
	}

	public boolean isReserved() {
		if (getStatus() == SeatHoldStatus.Reserved) {
			return true;
		}
		return false;

	}

	public boolean isExpired(int maxHoldLimit) {
		if (getStatus() == SeatHoldStatus.Reserved) {
			return false;
		}
		if (getStatus() == SeatHoldStatus.Expired) {
			return true;
		}
		return this.seatHoldTime.plusSeconds(maxHoldLimit).isBefore(LocalDateTime.now());
	}

	public boolean isHeld() {
		if (getStatus() == SeatHoldStatus.Held) {
			return true;
		}
		return false;
	}

	public synchronized SeatHoldStatus getStatus() {
		return status;
	}

	public synchronized void setStatus(SeatHoldStatus status) {
		this.status = status;
	}

}
