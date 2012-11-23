package ds02.server.event;

import java.util.ArrayList;
import java.util.List;

public class EventBus<T> {

	private final List<EventHandler<T>> handlers = new ArrayList<EventHandler<T>>();

	public void addHandler(EventHandler<T> handler) {
		handlers.add(handler);
	}

	public void removeHandler(EventHandler<T> handler) {
		handlers.remove(handler);
	}

	public void removeHandlers() {
		handlers.clear();
	}

	public void notify(T event) {
		for (int i = 0; i < handlers.size(); i++) {
			handlers.get(i).handle(event);
		}
	}
}
