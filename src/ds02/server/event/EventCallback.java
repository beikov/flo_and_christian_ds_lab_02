package ds02.server.event;

import java.rmi.Remote;


public interface EventCallback extends EventHandler<Event>, Remote {
	public void handle(Event event);
}
