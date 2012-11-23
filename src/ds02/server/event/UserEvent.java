package ds02.server.event;

public class UserEvent extends Event {
	private final String user;

	private static final long serialVersionUID = 1L;

	public UserEvent(String id, String type, long timeStamp, String user) {
		super(id, type, timeStamp);
		this.user = user;
	}

	public UserEvent(String type, String user) {
		super(type);
		this.user = user;
	}

	public String getUser() {
		return user;
	}
}
