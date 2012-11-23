package ds02.server.event;

public class AuctionTimeAvgEvent extends StatisticsEvent {

	public AuctionTimeAvgEvent(double value) {
		super("AUCTION_TIME_AVG", value);
	}
	
}
