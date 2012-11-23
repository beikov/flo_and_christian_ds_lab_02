package ds02.server.event;

public class UserEvent {
    private final String user;

    public UserEvent(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
