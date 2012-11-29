package ds02.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import ds02.client.command.AddStepCommand;
import ds02.client.command.AutoCommand;
import ds02.client.command.BillCommand;
import ds02.client.command.Command;
import ds02.client.command.HideCommand;
import ds02.client.command.LoginCommand;
import ds02.client.command.LogoutCommand;
import ds02.client.command.PrintCommand;
import ds02.client.command.RemoveStepCommand;
import ds02.client.command.StepsCommand;
import ds02.client.command.SubscribeCommand;
import ds02.client.command.UnsubscribeCommand;
import ds02.server.service.ServiceLocator;
import ds02.server.util.RuntimeUtils;

public class ManagementClientMain implements Client {

	private static final String[] NO_ARGS = new String[0];
	private final Map<String, Command> loggedInCommandMap = new HashMap<String, Command>();
	private final Map<String, Command> loggedOutCommandMap = new HashMap<String, Command>();
	private final UserContext context;

	private final BufferedReader in;
	private final PrintStream out;

	public ManagementClientMain(BufferedReader in, PrintStream out) {
		this.in = in;
		this.out = out;
		this.context = new UserContext(out);
		assembleCommands();
	}

	public void run() {
		String command;

		while (true) {
			command = readRequest();

			if (command == null || "!exit".equals(command)) {
				break;
			}

			final String[] commandParts = command.split("\\s");
			final String commandKey = commandParts[0];
			final String[] commandArgs;

			if (commandParts.length > 1) {
				commandArgs = new String[commandParts.length - 1];
				System.arraycopy(commandParts, 1, commandArgs, 0,
						commandArgs.length);
			} else {
				commandArgs = NO_ARGS;
			}

			final Command cmd = context.getUsername() == null ? loggedOutCommandMap
					.get(commandKey) : loggedInCommandMap.get(commandKey);

			if (cmd != null) {
				try {
					cmd.execute(context, commandArgs);
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
			} else {
				context.getOut().println("ERROR: Invalid command '" + commandKey + "'");
			}
		}

		RuntimeUtils.invokeShutdownHooks();
	}

	private String readRequest() {
		StringBuilder sb = new StringBuilder();

		if (context.isLoggedIn()) {
			sb.append(context.getUsername());

		}

		sb.append("> ");

		out.print(sb.toString());
		out.flush();

		try {
			return in.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	private void assembleCommands() {
		loggedOutCommandMap.put("!login", new LoginCommand());
		loggedOutCommandMap.put("!logout", new LogoutCommand());
		loggedOutCommandMap.put("!subscribe", new SubscribeCommand());
		loggedOutCommandMap.put("!unsubscribe", new UnsubscribeCommand());
		loggedOutCommandMap.put("!print", new PrintCommand());
		loggedOutCommandMap.put("!auto", new AutoCommand());
		loggedOutCommandMap.put("!hide", new HideCommand());

		loggedInCommandMap.put("!login", new LoginCommand());
		loggedInCommandMap.put("!logout", new LogoutCommand());
		loggedInCommandMap.put("!steps", new StepsCommand());
		loggedInCommandMap.put("!addStep", new AddStepCommand());
		loggedInCommandMap.put("!removeStep", new RemoveStepCommand());
		loggedInCommandMap.put("!bill", new BillCommand());
		loggedInCommandMap.put("!subscribe", new SubscribeCommand());
		loggedInCommandMap.put("!unsubscribe", new UnsubscribeCommand());
		loggedInCommandMap.put("!print", new PrintCommand());
		loggedInCommandMap.put("!auto", new AutoCommand());
		loggedInCommandMap.put("!hide", new HideCommand());
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			usage();
		}

		ServiceLocator.init(args[0], args[1]);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		new ManagementClientMain(in, System.out).run();
	}

	private static void usage() {
		System.err.println("Usage: "
				+ ManagementClientMain.class.getSimpleName()
				+ " <analyticsServerBinding> <billingServerBinding>");
		System.exit(1);
	}
}
