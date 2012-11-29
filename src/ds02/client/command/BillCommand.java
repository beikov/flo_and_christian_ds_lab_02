package ds02.client.command;

import java.rmi.RemoteException;

import ds02.client.UserContext;
import ds02.server.model.Bill;
import ds02.server.model.BillLine;

public class BillCommand implements Command {

	@Override
	public void execute(UserContext context, String[] args) {
		if (args.length != 1) {
			throw new RuntimeException("Usage: !bill <username>");
		}
		try {
			Bill bill = context.getBillingServiceSecure().getBill(args[0]);
			if (bill == null) {
				context.getOut().println("No bills for user " + args[0]
						+ " exist");
				return;
			}
			context.getOut()
					.println("auction_ID strike_price fee_fixed fee_variable fee_total");
			for (BillLine billLine : bill.getBillLines()) {
				context.getOut().format("%-11d%-13.0f%-10.1f%-13.1f%-9.1f%n",
						billLine.getAuctionId(), billLine.getStrikePrice(),
						billLine.getFeeFixed(), billLine.getFeeVariable(),
						billLine.getFeeTotal());
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
