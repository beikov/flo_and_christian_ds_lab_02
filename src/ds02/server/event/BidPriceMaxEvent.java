package ds02.server.event;

public class BidPriceMaxEvent extends StatisticsEvent {

	public BidPriceMaxEvent(double value) {
		super("BID_PRICE_MAX", value);
	}
	
}
