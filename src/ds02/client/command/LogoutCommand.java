package ds02.client.command;

import ds02.client.UserContext;

public class LogoutCommand implements Command {

	@Override
	public void execute(UserContext context, String[] args) {
		if(args.length != 0) {
			throw new RuntimeException("Usage: !logout");
		}
		if (!context.isLoggedIn()) {
			context.getOut().println("You have to log in first!");
		} else {
			context.getOut().println(context.getUsername()
					+ " successfully logged out");
			context.logout();
		}
	}
}
