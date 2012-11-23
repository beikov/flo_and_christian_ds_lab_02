package ds02.server.event;

public class BidEvent extends Event {
	private final String username;
	private final long auctionId;
	private final double price;
	private static final long serialVersionUID = 1L;

	public BidEvent(String id, String type, long timeStamp, String username,
			long auctionId, double price) {
		super(id, type, timeStamp);
		this.username = username;
		this.auctionId = auctionId;
		this.price = price;
	}

	public BidEvent(String type, String username, long auctionId, double price) {
		super(type);
		this.username = username;
		this.auctionId = auctionId;
		this.price = price;
	}

	public String getUsername() {
		return username;
	}

	public long getAuctionId() {
		return auctionId;
	}

	public double getPrice() {
		return price;
	}

}
