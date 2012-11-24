package ds02.server.event;

public class BidPriceMaxEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public BidPriceMaxEvent(double value) {
		super("BID_PRICE_MAX", value);
	}

	public String toString() {
		return super.toString() + "maximum bid price seen so far is "
				+ getValue();
	}
}
