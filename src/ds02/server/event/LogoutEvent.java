package ds02.server.event;

public class LogoutEvent extends UserEvent {
	private static final long serialVersionUID = 1L;

	public LogoutEvent(String user) {
		super("USER_LOGOUT", user);
	}

}
