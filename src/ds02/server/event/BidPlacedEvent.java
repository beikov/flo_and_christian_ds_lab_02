package ds02.server.event;

public class BidPlacedEvent extends BidEvent {
	private static final long serialVersionUID = 1L;

	public BidPlacedEvent(String username, long auctionId, double price) {
		super("BID_PLACED", username, auctionId, price);
	}

	public String toString() {
		return super.toString() + "new bid of value " + getPrice()
				+ " for auction " + getAuctionId();
	}
}
