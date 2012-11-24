package ds02.server.event;

public class BidWonEvent extends BidEvent {
	private static final long serialVersionUID = 1L;

	public BidWonEvent(String username, long auctionId, double price) {
		super("BID_WON", username, auctionId, price);
	}

	@Override
	public String toString() {
		return super.toString() + " auction " + getAuctionId() + " won by "
				+ getUsername() + " at a price of " + getPrice();
	}

}
