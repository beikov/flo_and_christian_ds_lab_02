package ds02.server.event;

public class UserSessiontimeMaxEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public UserSessiontimeMaxEvent(double value) {
		super("USER_SESSIONTIME_MAX", value);
	}

}
