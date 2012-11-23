package ds02.server.event;

public class UserSessiontimeAvgEvent extends StatisticsEvent {

	public UserSessiontimeAvgEvent(double value) {
		super("USER_SESSIONTIME_AVG", value);
	}
	
}
