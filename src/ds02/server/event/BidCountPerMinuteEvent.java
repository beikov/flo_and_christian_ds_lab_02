package ds02.server.event;

public class BidCountPerMinuteEvent extends StatisticsEvent {

	public BidCountPerMinuteEvent(double value) {
		super("BID_COUNT_PER_MINUTE", value);
	}
	
}
