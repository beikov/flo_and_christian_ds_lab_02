package ds02.server.event;

public class UserSessiontimeMinEvent extends StatisticsEvent {

	public UserSessiontimeMinEvent(double value) {
		super("USER_SESSIONTIME_MIN", value);
	}
	
}
