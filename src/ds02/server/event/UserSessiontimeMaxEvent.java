package ds02.server.event;

import ds02.server.service.impl.StatisticDataServiceImpl;

public class UserSessiontimeMaxEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public UserSessiontimeMaxEvent(double value) {
		super("USER_SESSIONTIME_MAX", value);
	}
	@Override
	public String toString() {
		return super.toString() + "maximum session time is "
				+ StatisticDataServiceImpl.INSTANCE.getLongestUserSessionTime()
				+ " seconds";
	}

}
