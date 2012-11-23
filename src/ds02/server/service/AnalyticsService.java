package ds02.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ds02.server.event.Event;
import ds02.server.event.EventHandler;

public interface AnalyticsService extends Remote {
	public String subscribe(String pattern, EventHandler<Event> handler) throws RemoteException;

	public void processEvent(Event event) throws RemoteException;
	
	public void unsubscribe(String identifier) throws RemoteException;
}
