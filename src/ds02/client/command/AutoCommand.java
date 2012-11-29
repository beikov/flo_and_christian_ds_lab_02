package ds02.client.command;

import ds02.client.UserContext;

public class AutoCommand implements Command {

	@Override
	public void execute(UserContext context, String[] args) {
		
		if (args.length != 0) {
			throw new RuntimeException("Usage: !auto");
		}
		new PrintCommand().execute(context, args);
		context.setAuto(true);
	}

}
