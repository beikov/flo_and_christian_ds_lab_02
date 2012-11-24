package ds02.client.command;

import java.rmi.RemoteException;

import ds02.client.UserContext;
import ds02.server.event.Event;
import ds02.server.event.EventCallback;
import ds02.server.service.ServiceLocator;
import ds02.server.util.RegistryUtils;

public class SubscribeCommand implements Command {

	@Override
	public void execute(final UserContext context, String[] args) {
		if (args.length != 1) {
			throw new RuntimeException("Dummkopf!");
		}
		
		EventCallback ec = RegistryUtils.exportObject(new EventCallback() {
			
			@Override
			public void handle(Event event) {
				context.addEvent(event);
			}
		}) ;
		
		try {
			ServiceLocator.INSTANCE.getAnalyticsService().subscribe(args[0], ec);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
