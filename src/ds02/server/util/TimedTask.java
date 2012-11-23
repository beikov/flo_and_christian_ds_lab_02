package ds02.server.util;

import java.util.Date;

public abstract class TimedTask implements Comparable<TimedTask> {

	private final Date date;

	public TimedTask(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public abstract void run();

	public int compareTo(TimedTask timedTask) {
		return this.date.compareTo(timedTask.getDate());
	}
}
