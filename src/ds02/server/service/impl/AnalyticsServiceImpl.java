package ds02.server.service.impl;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ds02.server.event.Event;
import ds02.server.event.EventHandler;
import ds02.server.event.UserEvent;
import ds02.server.service.AnalyticsService;

public class AnalyticsServiceImpl implements AnalyticsService {

	private static final long serialVersionUID = 1L;

	private ConcurrentMap<String, Long> startValue = new ConcurrentHashMap<String, Long>();

	private final Date startTime = new Date();

	@Override
	public String subscribe(String pattern, EventHandler<Event> handler)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processEvent(Event event) throws RemoteException {
		if ("USER_LOGIN".equals(event.getType())) {
			UserEvent userEvent = (UserEvent) event;
			startValue.put(userEvent.getUser(), userEvent.getTimeStamp());
			StatisticDataServiceImpl.INSTANCE.incrementSessionCount();
		} else if ("USER_LOGGOUT".equals(event.getType())
				|| "USER_DISCONNECTED".equals(event.getType())) {
			UserEvent userEvent = (UserEvent) event;
			if (startValue.containsKey(userEvent.getUser())) {
				StatisticDataServiceImpl.INSTANCE.addUserSessionTime(startValue.get(userEvent.getUser()));
				
			}
		} else if("BID_PLACED".equals(event.getType())) {
			StatisticDataServiceImpl.INSTANCE.incrementBidCount();
		}

	}

	@Override
	public void unsubscribe(String identifier) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
