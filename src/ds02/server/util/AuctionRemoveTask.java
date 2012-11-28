package ds02.server.util;

import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class AuctionRemoveTask extends TimerTask {

	private final ConcurrentNavigableMap<TimedTask, Object> taskQueue = new ConcurrentSkipListMap<TimedTask, Object>();
	private static final Object STUB = new Object();

	public void add(TimedTask task) {
		taskQueue.put(task, STUB);
	}

	@Override
	public void run() {
		final Date now = new Date();

		final Iterator<TimedTask> it = taskQueue.keySet().iterator();

		while (it.hasNext()) {
			final TimedTask task = it.next();

			if (task.getDate().after(now)) {
				return;
			}
			try {
				task.run();
			} catch (Throwable e) {
				e.printStackTrace(System.err);
			} finally {
				it.remove();
			}

		}

	}

	@Override
	public boolean cancel() {
		boolean result = super.cancel();
		taskQueue.clear();
		return result;
	}
}
