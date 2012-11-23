package ds02.server.command;

import ds02.server.UserConnection;
import ds02.server.service.UserService;

public class LoginCommand implements Command {

    private final UserService userService;

    public LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(UserConnection userConnection, String[] args) {
        String username = null;
        Integer udpPort = 0;
        
        if(args.length > 0){
            username = args[0];
        }
        
        if(args.length > 1){
            try{
                udpPort = Integer.parseInt(args[1]);
            }catch(NumberFormatException ex){
                /* Don't throw an exception */
            }
        }
        
        if (userConnection.isLoggedIn()) {
            userConnection.writeResponse("You have to log out first!");
        } else if (userService.login(username, userConnection)) {
            try {
                userConnection.login(username, udpPort);
                userConnection.writeResponse("Successfully logged in as " + username + "!");
            } catch (RuntimeException ex) {
                userService.logout(username);
                throw ex;
            }
        } else {
            userConnection.writeResponse("Already logged in");
        }
    }
}
