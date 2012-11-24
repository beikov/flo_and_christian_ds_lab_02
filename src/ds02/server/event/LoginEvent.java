package ds02.server.event;

public class LoginEvent extends UserEvent {
	private static final long serialVersionUID = 1L;

	public LoginEvent(String user) {
		super("USER_LOGIN", user);
	}

	@Override
	public String toString() {
		return super.toString() + " user " + getUser() + " logged in";
	}
}
