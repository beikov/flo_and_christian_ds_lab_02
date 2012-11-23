package ds02.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import ds02.client.command.Command;
import ds02.client.command.LoginCommand;
import ds02.client.command.LogoutCommand;

public class ManagementClientMain {

	private static final String[] NO_ARGS = new String[0];
	private final UserContext context = new UserContext();
	private Map<String, Command> loggedInCommandMap = new HashMap<String, Command>();
	private Map<String, Command> loggedOutCommandMap = new HashMap<String, Command>();
	
	private BufferedReader br = new BufferedReader(new InputStreamReader(
			System.in));

	public ManagementClientMain() {
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
				System.err.println("Invalid command '" + commandKey + "'");
			}
		}
	}

	private String readRequest() {
		StringBuilder sb = new StringBuilder();

		if (context.isLoggedIn()) {
			sb.append(context.getUsername());

		}

		sb.append("> ");

		System.out.println(sb.toString());
		System.out.flush();

		try {
			return br.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	private void assembleCommands() {
		loggedOutCommandMap.put("!login", new LoginCommand());
		loggedOutCommandMap.put("!logout", new LogoutCommand());
		
		
		loggedInCommandMap.put("!login", new LoginCommand());
		loggedInCommandMap.put("!logout", new LogoutCommand());
	}

	public static void main(String[] args) {
		new ManagementClientMain().run();
	}
}
