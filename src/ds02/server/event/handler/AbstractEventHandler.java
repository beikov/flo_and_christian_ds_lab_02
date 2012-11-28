package ds02.server.event.handler;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import ds02.server.event.Event;
import ds02.server.event.EventHandler;
import ds02.server.service.ServiceLocator;

public abstract class AbstractEventHandler<T extends Event> implements
		EventHandler<T> {
	
	private static final Logger LOG = Logger.getLogger(AbstractEventHandler.class);

	@Override
	public void handle(T event) {
		try {
			ServiceLocator.INSTANCE.getAnalyticsService().processEvent(event);
		} catch (RemoteException e) {
			LOG.warn("AnalyticsService is not reachable!");
		}

	}
}
