package ds02.client.command;

import ds02.client.UserContext;

public class RemoveStepCommand implements Command {

	@Override
	public void execute(UserContext context, String[] args) {
		if (args.length != 2) {
			throw new RuntimeException("Dummkopf!");
		}

		double startPrice = Double.parseDouble(args[0]);
		double endPrice = Double.parseDouble(args[1]);


		if (endPrice == 0 && endPrice < startPrice) {
			endPrice = Double.POSITIVE_INFINITY;
		}
		try {
			context.getBillingServiceSecure().deletePriceStep(startPrice, endPrice);
			
			System.out.println("Price step ["
					+ startPrice
					+ " "
					+ (endPrice == Double.POSITIVE_INFINITY ? "INFINITY"
							: endPrice) + "] successfully removed");
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

}
