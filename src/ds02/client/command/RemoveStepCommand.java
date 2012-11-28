package ds02.client.command;

import java.util.logging.Logger;

import ds02.client.UserContext;
import ds02.server.exception.PriceStepException;

public class RemoveStepCommand implements Command {

	private static final Logger LOG = Logger.getLogger("RemoveStepCommand");
	@Override
	public void execute(UserContext context, String[] args) {
		if (args.length != 2) {
			throw new RuntimeException("Usage: !removeStep <startPrice> <endPrice>");
		}

		double startPrice = Double.parseDouble(args[0]);
		double endPrice = Double.parseDouble(args[1]);

		if (endPrice == 0 && endPrice < startPrice) {
			endPrice = Double.POSITIVE_INFINITY;
		}
		try {
			context.getBillingServiceSecure().deletePriceStep(startPrice,
					endPrice);

			System.out.println("Price step ["
					+ startPrice
					+ " "
					+ (endPrice == Double.POSITIVE_INFINITY ? "INFINITY"
							: endPrice) + "] successfully removed");
		} catch (PriceStepException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			LOG.warning("Exception caught");
		}
	}

}
