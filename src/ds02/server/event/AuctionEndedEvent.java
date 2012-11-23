package ds02.server.event;

public class AuctionEndedEvent extends AuctionEvent {

	private static final long serialVersionUID = 1L;

	public AuctionEndedEvent(long auctionId) {
		super("AUCTION_ENDED", auctionId);
	}

}
