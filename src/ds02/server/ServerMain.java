package ds02.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ds02.server.event.DisconnectedEvent;
import ds02.server.event.EventHandler;
import ds02.server.service.AuctionService;
import ds02.server.service.ServiceLocator;

public class ServerMain {

	private static final int THREADS = 10000;

	public static void main(String[] args) {
		if (args.length != 3) {
			usage();
		}

		int port = 0;

		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			ex.printStackTrace(System.err);
			usage();
		}

		ServiceLocator.init(args[1], args[2]);
		BufferedReader in = null;

		try {
			in = new BufferedReader(new InputStreamReader(System.in));
		} catch (Exception ex) {
			// This normally should not happen
		}

		final ServerSocket serverSocket;

		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(port));
		} catch (Exception ex) {
			throw new RuntimeException("Could not create server socket!", ex);
		}

		final ExecutorService threadPool = Executors
				.newFixedThreadPool(THREADS);

		/* starts garbage collection */
		final Timer timer = new Timer();

		timer.scheduleAtFixedRate(AuctionService.REMOVE_TASK, 0, 1000);

		/*
		 * We use concurrent hash map for performance and because there is no
		 * ConcurrentHashSet
		 */
		final Map<ClientHandler, Object> clientHandlers = new ConcurrentHashMap<ClientHandler, Object>();

		/* The thread for accepting connections */
		new Thread() {
			@Override
			public void run() {
				while (!serverSocket.isClosed()) {
					try {
						final UserConnection connection = new UserConnection(
								serverSocket.accept());
						final ClientHandler handler = new ClientHandler(
								connection);

						clientHandlers.put(handler, new Object());

						connection
								.addCloseListener(new EventHandler<DisconnectedEvent>() {

									@Override
									public void handle(DisconnectedEvent event) {
										clientHandlers.remove(handler);
									}

								});

						threadPool.execute(handler);
					} catch (Exception ex) {
						// Don't care about the errors since logging is not
						// required
						// if(!serverSocket.isClosed()){
						// ex.printStackTrace(System.err);
						// }
					}
				}
			}
		}.start();

		final Runnable shutdownHook = new Runnable() {

			@Override
			public void run() {
				if (serverSocket != null) {
					try {
						serverSocket.close();
					} catch (Exception ex) {
						// Ignore
					}
				}

				if (threadPool != null) {
					threadPool.shutdown();
				}

				final Iterator<Map.Entry<ClientHandler, Object>> iter = clientHandlers
						.entrySet().iterator();

				while (iter.hasNext()) {
					final ClientHandler handler = iter.next().getKey();
					handler.stop();
					iter.remove();
				}

				timer.cancel();
			}
		};

		/* Also use runtime shutdown hook */
		Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));

		try {
			// Requirement states that a simple enter hit should end the server
			in.readLine();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

		shutdownHook.run();
	}

	private static void usage() {
		System.out.println("Usage: " + ServerMain.class.getSimpleName()
				+ " <tcpPort>");
		System.exit(1);
	}
}
