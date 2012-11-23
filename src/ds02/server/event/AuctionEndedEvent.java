package ds02.server.event;


public class AuctionEndedEvent extends AuctionEvent {

	public AuctionEndedEvent(long auctionId) {
		super("AUCTION_ENDED", auctionId);
	}
    
}
