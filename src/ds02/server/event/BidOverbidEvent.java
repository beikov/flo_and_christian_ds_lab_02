package ds02.server.event;

public class BidOverbidEvent extends BidEvent {

	public BidOverbidEvent(String username, long auctionId,
			double price) {
		super("BID_OVERBID", username, auctionId, price);
	}
	
}
