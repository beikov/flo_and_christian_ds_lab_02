package ds02.client.command;

import ds02.client.UserContext;

public interface Command {

	public void execute(UserContext context, String[] args);
}
