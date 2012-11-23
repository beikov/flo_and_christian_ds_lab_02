package ds02.server.command;

import java.util.List;

import ds02.server.UserConnection;
import ds02.server.model.Auction;
import ds02.server.service.AuctionService;
import ds02.server.util.Formats;

public class ListCommand implements Command {

	private final AuctionService bidService;

	public ListCommand(AuctionService bidService) {
		this.bidService = bidService;
	}

	@Override
	public void execute(UserConnection userConnection, String[] args) {
		final StringBuilder sb = new StringBuilder();
		final List<Auction> auctions = bidService.getAuctions();

		for (int i = 0; i < auctions.size(); i++) {
			if (i != 0) {
				sb.append('\n');
			}

			final Auction auction = auctions.get(i);
			sb.append(auction.getId()).append(". '");
			sb.append(auction.getDescription());
			sb.append("' ");
			sb.append(auction.getUser());
			sb.append(' ');
			sb.append(Formats.getDateFormat().format(
					auction.getEndTimestamp().getTime()));
			sb.append(' ');
			sb.append(Formats.getNumberFormat().format(auction.getBidValue()));
			sb.append(' ');
			sb.append(auction.getBidUser() == null ? "none" : auction
					.getBidUser());
		}

		userConnection.writeResponse(sb.toString());
	}
}
