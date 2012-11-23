package ds02.server.event;

public class BidWonEvent extends BidEvent {

	public BidWonEvent(String username, long auctionId,
			double price) {
		super("BID_WON", username, auctionId, price);
	}
	
}
