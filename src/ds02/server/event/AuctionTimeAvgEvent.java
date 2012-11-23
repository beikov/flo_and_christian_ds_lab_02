package ds02.server.event;

public class AuctionTimeAvgEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public AuctionTimeAvgEvent(double value) {
		super("AUCTION_TIME_AVG", value);
	}

}
