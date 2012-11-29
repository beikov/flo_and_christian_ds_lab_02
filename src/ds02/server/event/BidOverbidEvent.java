package ds02.server.event;

public class BidOverbidEvent extends BidEvent {
	private static final long serialVersionUID = 1L;

	public BidOverbidEvent(String username, long auctionId, double price) {
		super("BID_OVERBID", username, auctionId, price);
	}

	public String toString() {
		return super.toString() + "current user for auction " + getAuctionId()
				+ " was overbidded";
	}
}
