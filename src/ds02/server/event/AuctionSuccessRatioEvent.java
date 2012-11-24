package ds02.server.event;

public class AuctionSuccessRatioEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public AuctionSuccessRatioEvent(double value) {
		super("AUCTION_SUCCESS_RATIO", value);
	}

	@Override
	public String toString() {

		return super.toString() + "auction success ratio is " + getValue();
	}

}
