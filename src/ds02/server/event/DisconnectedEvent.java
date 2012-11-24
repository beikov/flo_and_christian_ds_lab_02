package ds02.server.event;

public class DisconnectedEvent extends UserEvent {
	private static final long serialVersionUID = 1L;

	public DisconnectedEvent(String user) {
		super("USER_DISCONNECTED", user);
	}

	@Override
	public String toString() {
		return super.toString() + " user " + getUser() + " was disconnected";
	}

}
