package ds02.server.event;

public class UserSessiontimeMaxEvent extends StatisticsEvent {

	public UserSessiontimeMaxEvent(double value) {
		super("USER_SESSIONTIME_MAX", value);
	}
	
}
