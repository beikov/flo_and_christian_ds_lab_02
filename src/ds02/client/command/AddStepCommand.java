package ds02.client.command;

import ds02.client.UserContext;

public class AddStepCommand implements Command {

	@Override
	public void execute(UserContext context, String[] args) {
		if (args.length != 4) {
			throw new RuntimeException("Dummkopf!");
		}

		double startPrice = Double.parseDouble(args[0]);
		double endPrice = Double.parseDouble(args[1]);
		double fixedPrice = Double.parseDouble(args[2]);
		double variablePricePercent = Double.parseDouble(args[3]);

		if (endPrice == 0 && endPrice < startPrice) {
			endPrice = Double.POSITIVE_INFINITY;
		}
		try {
			context.getBillingServiceSecure().createPriceStep(startPrice,
					endPrice, fixedPrice, variablePricePercent);
			System.out.println("Step ["
					+ startPrice
					+ " "
					+ (endPrice == Double.POSITIVE_INFINITY ? "INFINITY"
							: endPrice) + "] successfully added");
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

}
