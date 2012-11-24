package ds02.client.command;

import java.rmi.RemoteException;

import ds02.client.UserContext;
import ds02.server.model.Bill;
import ds02.server.model.BillLine;

public class BillCommand implements Command {

	@Override
	public void execute(UserContext context, String[] args) {
		if (args.length != 1) {
			throw new RuntimeException("Dummkopf!");
		}
		try {
			Bill bill = context.getBillingServiceSecure().getBill(args[0]);
			System.out
					.println("auction_ID strike_price fee_fixed fee_variable fee_total");
			for (BillLine billLine : bill.getBillLines()) {
				System.out.format("%.0d %.0f %.0f %.1f %.1f",
						billLine.getAuctionId(), billLine.getStrikePrice(),
						billLine.getFeeFixed(), billLine.getFeeVariable(),
						billLine.getFeeTotal());
				System.out.println("%");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
