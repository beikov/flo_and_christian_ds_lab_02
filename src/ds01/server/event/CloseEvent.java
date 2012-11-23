package ds01.server.event;

public class CloseEvent extends UserEvent{

    public CloseEvent(String user) {
        super(user);
    }
}
