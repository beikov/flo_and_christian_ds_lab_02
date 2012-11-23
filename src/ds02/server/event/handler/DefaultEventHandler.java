package ds02.server.event.handler;

import ds02.server.event.Event;

public class DefaultEventHandler extends AbstractEventHandler<Event> {

	public static DefaultEventHandler INSTANCE = new DefaultEventHandler();

	private DefaultEventHandler() {

	}
}
