package ds02.server.event;

import ds02.server.service.impl.StatisticDataServiceImpl;

public class BidCountPerMinuteEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public BidCountPerMinuteEvent(double value) {
		super("BID_COUNT_PER_MINUTE", value);
	}

	public String toString() {
		return super.toString() + "current bid per minute is "
				+ StatisticDataServiceImpl.INSTANCE.getBidCountPerMinute();
	}
}
