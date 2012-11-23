package ds01.server.command;

import ds01.server.UserConnection;

public interface Command {
    
    public void execute(UserConnection userConnection, String[] args);
}
