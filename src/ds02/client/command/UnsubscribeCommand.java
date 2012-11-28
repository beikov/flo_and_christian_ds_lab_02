package ds02.client.command;

import java.rmi.RemoteException;

import ds02.client.UserContext;
import ds02.server.service.ServiceLocator;

public class UnsubscribeCommand implements Command {

	@Override
	public void execute(UserContext context, String[] args) {
		if (args.length != 1) {
			throw new RuntimeException("Usage: !unsubscribe <subscriptionID>");
		}

		try {
			ServiceLocator.INSTANCE.getAnalyticsService().unsubscribe(args[0]);
			System.out.println("subscription " + args[0] + " terminated");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

}
