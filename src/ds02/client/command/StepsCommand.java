package ds02.client.command;

import java.rmi.RemoteException;
import java.util.Iterator;

import ds02.client.UserContext;
import ds02.server.model.PriceStep;

public class StepsCommand implements Command {

	@Override
	public void execute(UserContext context, String[] args) {
		
		try {
			System.out.println("Min_Price Max_Price Fee_Fixed Fee_Variable");
			for(PriceStep priceStep : context.getBillingServiceSecure().getPriceSetps().getPriceSteps()){
				System.out.format("%.0f\t  %s" + (priceStep.getEndPrice() == Double.POSITIVE_INFINITY ? "" : "\t   ") + " %.1f     %.1f", priceStep.getStartPrice(), (priceStep.getEndPrice() == Double.POSITIVE_INFINITY ? "INIFINITY" : (int) priceStep.getEndPrice()), priceStep.getFixedPrice(), priceStep.getVariablePricePercent());
				System.out.println("%");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
