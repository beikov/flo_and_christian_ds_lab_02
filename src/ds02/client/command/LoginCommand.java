package ds02.client.command;

import org.apache.log4j.Logger;

import ds02.client.UserContext;
import ds02.server.service.BillingService;
import ds02.server.service.BillingServiceSecure;
import ds02.server.util.RegistryUtils;

public class LoginCommand implements Command {
	private static final Logger LOG = Logger.getLogger(LoginCommand.class);
	@Override
	public void execute(UserContext context, String[] args) {
		BillingServiceSecure billingServiceSecure = null;

		String username = null;
		String password = null;

		if (args.length != 2) {
			throw new RuntimeException("You stink!");
		}

		username = args[0];
		password = args[1];
		try {
			if (context.isLoggedIn()) {
				System.out.println("ERROR: You are currently not logged in.");

			} else if ((billingServiceSecure = RegistryUtils.getRemote(
					BillingService.class).login(username, password)) != null) {

				context.login(username, billingServiceSecure);
				
				System.out.println(username + " successfully logged in");
			} else {
				System.out.println("Login failed");
			}
		} catch (Exception e) {
			LOG.error("Failed to get remote object", e);
		}
	}
}
