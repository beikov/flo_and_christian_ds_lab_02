package ds02.server.event;


public class LoginEvent extends UserEvent {

    public LoginEvent(String user) {
        super("USER_LOGIN", user);
    }
    
}
