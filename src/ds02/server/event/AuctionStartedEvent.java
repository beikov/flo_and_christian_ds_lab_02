package ds02.server.event;

public class AuctionStartedEvent extends AuctionEvent {
	private static final long serialVersionUID = 1L;

	public AuctionStartedEvent(long auctionId) {
		super("AUCTION_STARTED", auctionId);
	}

	@Override
	public String toString() {
		return super.toString() + "auction with id " + getAuctionId()
				+ " has started";
	}

}
