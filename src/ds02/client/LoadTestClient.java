package ds02.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ds02.server.util.ClientConsole;
import ds02.server.util.PipedClientConsole;
import ds02.server.util.PropertiesUtils;
import ds02.server.util.RuntimeUtils;

public class LoadTestClient implements Client {

	private final Map<Client, Streams> clients = new HashMap<Client, Streams>();
	private final ManagementClientMain managementClient;
	private final PrintStream managementOut;
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
				/* We can write with this stream to "stdin" */
				final PipedOutputStream posIn = new PipedOutputStream();
				final PipedInputStream pisIn = new PipedInputStream(posIn);

				/* This is the input stream for the client */
				final BufferedReader in = new BufferedReader(
						new InputStreamReader(pisIn));

				/*
				 * This is the outputstream for the client. We probably should
				 * have used our own interface but that's good enough for now.
				 */
				final PipedClientConsole out = ClientConsole.piped();

				clients.put(new ClientMain(in, out, host, tcpPort),
						new Streams(out, posIn));
			}

			/* We can write with this stream to "stdin" */
			final PipedOutputStream posIn = new PipedOutputStream();
			final PipedInputStream pisIn = new PipedInputStream(posIn);

			/* This is the input stream for the client */
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					pisIn));

			managementClient = new ManagementClientMain(in, System.out);
			managementOut = new PrintStream(posIn);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static class Streams {
		private final PipedClientConsole in;
		private final PrintStream out;

		public Streams(PipedClientConsole in, OutputStream out) throws IOException {
			this.in = in;
			this.out = new PrintStream(out);
		}

		public synchronized String command(String command) {
			/* Read the prompt */
			try {
				in.read();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}

			out.println(command);
			out.flush();
			
			try {
				return in.read();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public void run() {
		final double startTime = (double) System.currentTimeMillis();
		final ScheduledExecutorService schedulerService = Executors
				.newScheduledThreadPool(clients.size());
		final ExecutorService threadPool = Executors.newFixedThreadPool(clients
				.size() + 1);
		final AtomicLong auctionCount = new AtomicLong();

		RuntimeUtils.addShutdownHook(new Runnable() {

			@Override
			public void run() {
				schedulerService.shutdownNow();
				threadPool.shutdownNow();
			}
		});

		/* Let the management client run */
		threadPool.execute(managementClient);

		final PrintStream managementOut = this.managementOut;
		/* Simulate management client user */
		managementOut.println("!login john dslab2012");
		managementOut.println("!subscribe .*");
		managementOut.println("!auto");
		managementOut.flush();

		try {
			/* Wait here for the management client to process everything */
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			return;
		}

		int i = 0;

		for (final Map.Entry<Client, Streams> entry : clients.entrySet()) {
			i++;
			/* Let the client run */
			threadPool.execute(entry.getKey());

			final Random random = new Random();
			final int clientNumber = i;
			final Streams clientStream = entry.getValue();
			final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
			final List<AuctionEntry> clientBasedListResult = new ArrayList<AuctionEntry>();

			schedulerService.execute(new Runnable() {

				@Override
				public void run() {
					clientStream.command("!login client" + clientNumber);

					schedulerService.scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							final String result;

							try {
								result = clientStream.command("!list");
							} catch (Exception ex) {
								if (!schedulerService.isShutdown()) {
									System.err
											.println("Failed to list auctions");
								}
								return;
							}

							lock.writeLock().lock();

							try {
								clientBasedListResult.clear();

								if (!result.isEmpty()) {
									final String[] lines = result.split("\n");

									for (String line : lines) {
										final int lastSpaceIndex = line
												.lastIndexOf(' ');
										final double highestValue = Double
												.parseDouble(line.substring(
														line.lastIndexOf(
																' ',
																lastSpaceIndex - 1) + 1,
														lastSpaceIndex));
										clientBasedListResult
												.add(new AuctionEntry(line
														.substring(0, line
																.indexOf('.')),
														highestValue));
									}
								}
							} finally {
								lock.writeLock().unlock();
							}
						}
					}, 0, updateIntervalSeconds, TimeUnit.SECONDS);

					schedulerService.scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							lock.readLock().lock();

							try {
								if (!clientBasedListResult.isEmpty()) {
									final int index = random
											.nextInt(clientBasedListResult
													.size());
									final AuctionEntry entry = clientBasedListResult
											.get(index);
									final double bidValue = entry.lastHighestValue
											+ (((double) System
													.currentTimeMillis()) - startTime)
											/ 1000;
									clientStream.command("!bid " + entry.id
											+ " " + bidValue);
								}
							} catch (Exception ex) {
								if (!schedulerService.isShutdown()) {
									System.err
											.println("Failed to bid for auction: "
													+ ex.getMessage());
								}
							} finally {
								lock.readLock().unlock();
							}
						}
					}, 60000 / bidsPerMinute, 60000 / bidsPerMinute, TimeUnit.MILLISECONDS);

					schedulerService.scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							try {
								clientStream.command("!create "
										+ auctionDuration + " Client "
										+ clientNumber + " auction "
										+ auctionCount.incrementAndGet());
							} catch (Exception ex) {
								if (!schedulerService.isShutdown()) {
									System.err
											.println("Failed to create auction");
									ex.printStackTrace(System.err);
									System.exit(1);
								}
							}
						}
					}, 0, 60000 / auctionsPerMinute, TimeUnit.MILLISECONDS);
				}
			});

		}

		RuntimeUtils.waitForExitCommand();
	}

	private static class AuctionEntry {
		private final String id;
		private final double lastHighestValue;

		public AuctionEntry(String id, double lastHighestValue) {
			this.id = id;
			this.lastHighestValue = lastHighestValue;
		}
	}
}
