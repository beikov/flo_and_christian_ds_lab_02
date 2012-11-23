package ds01.server.command;

import ds01.server.UserConnection;
import ds01.server.service.UserService;

public class LogoutCommand implements Command {
    
    private final UserService userService;

    public LogoutCommand(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public void execute(UserConnection userConnection, String[] args){
        if(!userConnection.isLoggedIn()){
            userConnection.writeResponse("You have to log in first!");
        } else {
            userService.logout(userConnection.getUsername());
            userConnection.writeResponse("Successfully logged out as " + userConnection.getUsername() + "!");
            userConnection.logout();
        }
    }
}
