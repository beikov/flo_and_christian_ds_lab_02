package ds02.server.event;

import ds02.server.service.impl.StatisticDataServiceImpl;

public class UserSessiontimeMinEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public UserSessiontimeMinEvent(double value) {
		super("USER_SESSIONTIME_MIN", value);
	}
	@Override
	public String toString() {
		return super.toString() + "average session time is "
				+ StatisticDataServiceImpl.INSTANCE.getAverageUserSessionTime()
				+ " seconds";
	}

}
