package ds02.server.service.impl;

import java.rmi.RemoteException;

import ds02.server.event.Event;
import ds02.server.event.EventHandler;
import ds02.server.service.AnalyticsService;

public class AnalyticsServiceImpl implements AnalyticsService {

	@Override
	public String subscribe(String pattern, EventHandler<Event> handler)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processEvent(Event event) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsubscribe(String identifier) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
