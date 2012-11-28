package ds02.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ds02.server.util.PropertiesUtils;
import ds02.server.util.RuntimeUtils;

public class LoadTestClient implements Client {

	private final Map<Client, PrintStream> clients = new HashMap<Client, PrintStream>();
	private final ManagementClientMain managementClient;
	private final PrintStream managementWriter;
	private final long auctionsPerMinute;
	private final long auctionDuration;
	private final long updateIntervalSeconds;
	private final long bidsPerMinute;

	public LoadTestClient(String host, int tcpPort) {
		Properties props = PropertiesUtils.getProperties("loadtest.properties");
		long clientCount = Long.parseLong(props.getProperty("clients"));
		this.auctionsPerMinute = Long.parseLong(props
				.getProperty("auctionsPerMin"));
		this.auctionDuration = Long.parseLong(props
				.getProperty("auctionDuration"));
		this.updateIntervalSeconds = Long.parseLong(props
				.getProperty("updateIntervalSec"));
		this.bidsPerMinute = Long.parseLong(props.getProperty("bidsPerMin"));

		try {
			for (int i = 0; i < clientCount; i++) {
				final PipedOutputStream pos = new PipedOutputStream();
				final PipedInputStream pis = new PipedInputStream(pos);
				final BufferedReader in = new BufferedReader(
						new InputStreamReader(pis));
				clients.put(new ClientMain(in, host, tcpPort), new PrintStream(
						pos));
			}

			final PipedOutputStream pos = new PipedOutputStream();
			final PipedInputStream pis = new PipedInputStream(pos);
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					pis));

			managementClient = new ManagementClientMain(in);
			managementWriter = new PrintStream(pos);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void run() {
		final ExecutorService threadPool = Executors.newFixedThreadPool(clients
				.size() * 2 + 1);

		/* Let the management client run */
		threadPool.execute(managementClient);

		/* Simulate management client user */
		managementWriter.println("!auto");
		managementWriter.flush();
		managementWriter.println("!login john dslab2012");
		managementWriter.flush();

		int i = 0;

		for (final Map.Entry<Client, PrintStream> entry : clients.entrySet()) {
			i++;
			/* Let the client run */
			threadPool.execute(entry.getKey());

			final int clientNumber = i;

			/* Simulate client user */
			threadPool.execute(new Runnable() {

				@Override
				public void run() {
					final PrintStream out = entry.getValue();
					final long sleepTime = (long) (60000 / auctionsPerMinute);
					int auctionNumber = 1;

					out.println("!login client" + clientNumber);
					out.flush();

					while (!threadPool.isShutdown()) {
						out.println("!create " + auctionDuration + " Client "
								+ clientNumber + " auction " + auctionNumber++);
						out.flush();

						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException ex) {
							// We got woke up :D
						}
					}

					out.println("!logout");
					out.println("!exit");
				}
			});

		}

		RuntimeUtils.addShutdownHook(new Runnable() {
			
			@Override
			public void run() {
				threadPool.shutdownNow();
			}
		});
		RuntimeUtils.waitForExitCommand();
	}

}
