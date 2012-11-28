package ds02.server.command;

import ds02.server.UserConnection;

public class LogoutCommand implements Command {

	@Override
	public void execute(UserConnection userConnection, String[] args) {
		if (!userConnection.isLoggedIn()) {
			userConnection.writeResponse("You have to log in first!");
		} else {
			userConnection.writeResponse("Successfully logged out as "
					+ userConnection.getUsername() + "!");
			userConnection.logout();
		}
	}
}
