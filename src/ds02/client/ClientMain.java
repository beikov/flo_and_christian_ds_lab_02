package ds02.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;

import ds02.server.service.ServiceLocator;
import ds02.server.util.AuctionProtocolStream;
import ds02.server.util.ClientConsole;

public class ClientMain implements Client {

	private static final Logger LOG = Logger.getLogger(ClientMain.class);
	private final BufferedReader in;
	private final ClientConsole out;
	/* Socket connection to server */
	private final Socket socket;
	private final AuctionProtocolStream protocolStream;
	private final Runnable shutdownHook;

	public ClientMain(BufferedReader in, ClientConsole out, String host, int tcpPort) {
		this.in = in;
		this.out = out;

		try {
			this.socket = new Socket(host, tcpPort);
			this.protocolStream = new AuctionProtocolStream(socket.getOutputStream(), socket.getInputStream());
		} catch (Exception ex) {
			throw new RuntimeException("Could not connect to server", ex);
		}

		shutdownHook = new Runnable() {

			@Override
			public void run() {

				if (ClientMain.this.socket != null) {
					try {
						ClientMain.this.socket.close();
					} catch (Exception ex1) {
						// Ignore
					}
				}

			}
		};

		Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));
	}

	public void run() {
		String user = null;
		String command;
		String prompt = "> ";

		while (true) {
			try {
				command = prompt(prompt);
			} catch (Exception ex) {
				command = null;
			}

			if (command == null || "!exit".equals(command)) {
				break;
			}

			final String[] commandParts = command.split("\\s+");
			final boolean loginCommand = "!login".equals(commandParts[0]);
			final boolean logoutCommand = "!logout".equals(commandParts[0]);

			try {
				if (loginCommand) {
					if (user == null && commandParts.length > 1) {
						/*
						 * Extract user name out of the command so we can show
						 * it in the prompt and also start the notification
						 * handler
						 */
						user = commandParts[1];
					}
				}

				protocolStream.write(command);

				/*
				 * In case we get a ClassCastException, we don't care since we
				 * have to terminate the connection and so on anyway
				 */
				final String result = protocolStream.read();

				if (logoutCommand
						|| (loginCommand && !result.contains("Successfully") && user != null)) {
					/* Here we have either a logout or an unsuccessful login */
					user = null;
					prompt = "> ";
				} else if (user != null) {
					/* Successful login */
					prompt = user + "> ";
				}

				out.write(result);
			} catch (Exception ex) {
				try{
					out.write("Server terminated connection");
				} catch (Exception e){
					// Ignore since we will exit anyways
				}
				
				break;
			}
		}

		shutdownHook.run();
	}

	public static void main(String[] args) {
		if (args.length != 2 && args.length != 3) {
			usage();
		}

		String host = args[0];
		int tcpPort = 0;

		try {
			tcpPort = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			LOG.error(ex);
			usage();
		}

		final Client client;

		try {
			if (args.length == 3) {
				/* Create load test */
				ServiceLocator.init(args[2], null);
				client = new LoadTestClient(host, tcpPort);
			} else {
				final BufferedReader in = new BufferedReader(
						new InputStreamReader(System.in));
				
				client = new ClientMain(in, ClientConsole.out, host, tcpPort);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Could not instantiate client", ex);
		}

		client.run();
	}

	private static void usage() {
		System.out.println("Usage: " + ClientMain.class.getSimpleName()
				+ " <host> <tcpPort>");
		System.exit(1);
	}

	public String prompt(String prompt)
			throws Exception {
		out.write(prompt);
		return in.readLine();
	}
}
