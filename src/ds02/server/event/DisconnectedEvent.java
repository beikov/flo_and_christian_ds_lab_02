package ds02.server.event;

public class DisconnectedEvent extends UserEvent {
    public DisconnectedEvent(String user) {
        super("USER_DISCONNECTED", user);
    }
    
}
