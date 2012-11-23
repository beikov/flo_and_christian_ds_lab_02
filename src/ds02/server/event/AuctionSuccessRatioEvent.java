package ds02.server.event;

public class AuctionSuccessRatioEvent extends StatisticsEvent {

	public AuctionSuccessRatioEvent(double value) {
		super("AUCTION_SUCCESS_RATIO", value);
	}
	
}
