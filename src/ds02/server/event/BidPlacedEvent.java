package ds02.server.event;

public class BidPlacedEvent extends BidEvent {

	public BidPlacedEvent(String username, long auctionId,
			double price) {
		super("BID_PLACED", username, auctionId, price);
	}
	
}
