package ds02.server.event;

import ds02.server.service.impl.StatisticDataServiceImpl;

public class UserSessiontimeAvgEvent extends StatisticsEvent {
	private static final long serialVersionUID = 1L;

	public UserSessiontimeAvgEvent(double value) {
		super("USER_SESSIONTIME_AVG", value);
	}

	@Override
	public String toString() {
		return super.toString() + "minimum session time is "
				+ StatisticDataServiceImpl.INSTANCE.getMinUserSessionTime()
				+ " seconds";
	}

}
