package ds02.server.event;

public class BidCountPerMinuteEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public BidCountPerMinuteEvent(double value) {
		super("BID_COUNT_PER_MINUTE", value);
	}

}
