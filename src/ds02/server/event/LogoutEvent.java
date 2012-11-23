package ds02.server.event;

public class LogoutEvent extends UserEvent {

    public LogoutEvent(String user) {
        super(user);
    }
    
}
