package ds02.server.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class RuntimeUtils {

	private static final Queue<Runnable> SHUTDOWN_HOOKS = new ConcurrentLinkedQueue<Runnable>();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				invokeShutdownHooks();
			}

		});
	}

	private RuntimeUtils() {

	}

	public static void invokeShutdownHooks() {
		Iterator<Runnable> it = SHUTDOWN_HOOKS.iterator();

		while (it.hasNext()) {

			final Runnable r = it.next();

			try {
				r.run();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}

			it.remove();
		}
	}

	public static void addShutdownHook(Runnable r) {
		SHUTDOWN_HOOKS.add(r);
	}

	public static void waitForExitCommand() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String input = null;

		do {
			try {
				input = reader.readLine();
			} catch (Exception e) {
				// ignore
			}
		} while (!"!exit".equals(input) && input != null);
		invokeShutdownHooks();
	}
}
