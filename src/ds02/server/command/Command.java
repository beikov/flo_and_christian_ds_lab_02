package ds02.server.command;

import ds02.server.UserConnection;

public interface Command {

	public void execute(UserConnection userConnection, String[] args);
}
