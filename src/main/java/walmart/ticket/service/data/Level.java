package walmart.ticket.service.data;

/**
 * encapsulate Level config detail.
 * @author deep
 *
 */
public  class Level implements Comparable<Level> {
	private final int id;
	private final String name;
	private final double price;
	private final int noOfRows;
	private final int seatPerRow;

	/**
	 * Constructor.
	 * @param idArg
	 * @param nameArg
	 * @param priceArg
	 * @param noOfRowsArg
	 * @param seatPerRowArg
	 */
	public Level(final int idArg, final String nameArg, final double priceArg, final int noOfRowsArg,
			final int seatPerRowArg) {
		this.id = idArg;
		this.name = nameArg;
		this.price = priceArg;
		this.noOfRows = noOfRowsArg;
		this.seatPerRow = seatPerRowArg;

	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public int getSeatPerRow() {
		return seatPerRow;
	}
	
	public int seatsCount(){
		return noOfRows * seatPerRow;
	}

	@Override
	public int compareTo(Level levelArg) {
		return Integer.compare(this.id, levelArg.id);
	
	}

}
