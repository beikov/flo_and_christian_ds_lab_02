package ds02.server.event.handler;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ds02.server.event.Event;
import ds02.server.event.EventHandler;
import ds02.server.service.AnalyticsService;

public abstract class AbstractEventHandler<T extends Event> implements EventHandler<T> {

	@Override
	public void handle(T event) {
		Registry registry = null;
		
		try {
			registry = LocateRegistry.getRegistry(1099);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AnalyticsService analyticsService = null;
		
		try {
			analyticsService = (AnalyticsService) registry.lookup(AnalyticsService.class.getName());
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			analyticsService.processEvent(event);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
}
