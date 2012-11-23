package ds02.server.event;

public class AuctionStartedEvent extends AuctionEvent {

	public AuctionStartedEvent(long auctionId) {
		super("AUCTION_STARTED", auctionId);
	}

}
