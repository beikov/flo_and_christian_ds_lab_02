package ds02.server.event;

public class AuctionEvent extends Event {
	private final long auctionId;
	private static final long serialVersionUID = 1L;

	public AuctionEvent(String id, String type, long timeStamp, long auctionId) {
		super(id, type, timeStamp);
		this.auctionId = auctionId;
	}

	public AuctionEvent(String type, long auctionId) {
		super(type);
		this.auctionId = auctionId;
	}

	public long getAuctionId() {
		return auctionId;
	}

}
