package ds02.server.event.handler;

import java.rmi.RemoteException;

import ds02.server.event.Event;
import ds02.server.event.EventHandler;
import ds02.server.service.ServiceLocator;

public abstract class AbstractEventHandler<T extends Event> implements
		EventHandler<T> {

	@Override
	public void handle(T event) {
		try {
			ServiceLocator.INSTANCE.getAnalyticsService().processEvent(event);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}
