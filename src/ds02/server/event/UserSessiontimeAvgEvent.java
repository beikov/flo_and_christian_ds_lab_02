package ds02.server.event;

public class UserSessiontimeAvgEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public UserSessiontimeAvgEvent(double value) {
		super("USER_SESSIONTIME_AVG", value);
	}

	@Override
	public String toString() {
		return super.toString() + "average session time is " + getValue()
				/ 1000 + " seconds";
	}

}
