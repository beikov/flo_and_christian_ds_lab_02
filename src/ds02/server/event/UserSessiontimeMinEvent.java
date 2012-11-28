package ds02.server.event;

public class UserSessiontimeMinEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public UserSessiontimeMinEvent(double value) {
		super("USER_SESSIONTIME_MIN", value);
	}

	@Override
	public String toString() {
		return super.toString() + "minimum session time is " + getValue()
				/ 1000 + " seconds";
	}

}
