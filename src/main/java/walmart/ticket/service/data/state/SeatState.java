package walmart.ticket.service.data.state;
import com.google.common.collect.ComparisonChain;

import walmart.ticket.service.ISeatHoldExpired;
import walmart.ticket.service.data.Level;
import walmart.ticket.service.data.Seat;
import walmart.ticket.service.data.SeatStatus;

public class SeatState implements Comparable<SeatState>{

	private final Seat seat;
	private final ISeatHoldExpired seatholdExpired;
	private SeatStatus status = SeatStatus.Available;
	
	public SeatState(final Seat seatArg, final ISeatHoldExpired seatholdExpiredArg){
	 this.seat = seatArg;
	 this.seatholdExpired = seatholdExpiredArg;
	}


	@Override
	public int hashCode() {
		return this.seat.hashCode();
	}


	public Level getLevel() {
		return this.seat.getLevel();
	}


	public int getSeatNumber() {
		return this.seat.getSeatNumber();
	}


	public int getRowNumber() {
		return this.seat.getRowNumber();
	}
	
	public Seat getSeat(){
		return this.seat;
	}
	public int getSeatPositionRank() {
		return this.seat.getSeatPositionRank();
	}
	
	public synchronized SeatStatus getStatus() {
		return status;
	}


	public synchronized void setStatus(SeatStatus status) {
		this.status = status;
	}
    
	/**
	 * Seat Hold expired, Release seat back to Level store.
	 */
	public void release(){
		this.seatholdExpired.seatHoldExpired(this);
		this.status = SeatStatus.Available;
	}

	@Override
	public int compareTo(SeatState seatArg) {
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
