package ds02.server.event;

public class LogoutEvent extends UserEvent {
	private static final long serialVersionUID = 1L;

	public LogoutEvent(String user) {
		super("USER_LOGOUT", user);
	}

	@Override
	public String toString() {
		return super.toString() + " user " + getUser() + " logged out";
	}

}
