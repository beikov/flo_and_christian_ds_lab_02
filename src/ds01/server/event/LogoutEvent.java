package ds01.server.event;

public class LogoutEvent extends UserEvent {

    public LogoutEvent(String user) {
        super(user);
    }
    
}
