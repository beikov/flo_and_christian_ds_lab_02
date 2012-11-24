package ds02.server.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import ds02.server.event.Event;
import ds02.server.event.EventCallback;
import ds02.server.util.Pingable;

public interface AnalyticsService extends Remote, Serializable, Pingable {
	public String subscribe(String pattern, EventCallback handler)
			throws RemoteException;

	public void processEvent(Event event) throws RemoteException;

	public void unsubscribe(String identifier) throws RemoteException;
}
