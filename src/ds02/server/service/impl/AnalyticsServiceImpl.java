package ds02.server.service.impl;

import java.io.PrintStream;
import java.io.Serializable;
import java.rmi.RemoteException;

import ds02.server.event.Event;
import ds02.server.event.EventHandler;
import ds02.server.service.AnalyticsService;

public class AnalyticsServiceImpl implements AnalyticsService, Serializable {

	private static final PrintStream ps = System.out;

	@Override
	public String subscribe(String pattern, EventHandler<Event> handler)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processEvent(Event event) throws RemoteException {
		ps.println(event);
	}

	@Override
	public void unsubscribe(String identifier) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
