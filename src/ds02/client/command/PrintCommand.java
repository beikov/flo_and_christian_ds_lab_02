package ds02.client.command;

import ds02.client.UserContext;
import ds02.server.event.Event;

public class PrintCommand implements Command {

	@Override
	public void execute(UserContext context, String[] args) {
		if (args.length != 0) {
			throw new RuntimeException("Dummklpg");
		}
		StringBuilder sb = new StringBuilder();
		for (Event e : context.popEventQueue()) {
			sb.append(e.toString());
			sb.append("\n");
		}
		// TODO Event toString implementieren
		context.getOut().print(sb.toString());
	}

}
