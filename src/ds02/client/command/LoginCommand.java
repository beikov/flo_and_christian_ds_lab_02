package ds02.client.command;

import org.apache.log4j.Logger;

import ds02.client.UserContext;
import ds02.server.service.BillingServiceSecure;
import ds02.server.service.ServiceLocator;

public class LoginCommand implements Command {
	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public void execute(UserContext context, String[] args) {
		BillingServiceSecure billingServiceSecure = null;

		String username = null;
		String password = null;

		if (args.length != 2) {
			throw new RuntimeException("Usage: !login <username> <password>");
		}

		username = args[0];
		password = args[1];
		try {
			if (context.isLoggedIn()) {
				context.getOut().println("ERROR: You are currently not logged in.");

			} else if ((billingServiceSecure = ServiceLocator.INSTANCE.getBillingService().login(username, password)) != null) {

				context.login(username, billingServiceSecure);

				context.getOut().println(username + " successfully logged in");
			} else {
				System.err.println("ERROR: Login failed");
			}
		} catch (Exception e) {
			LOG.error("Failed to get remote object", e);
		}
	}
}
