package ds02.server.event;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Event implements Serializable {
	private final static AtomicLong uid = new AtomicLong();
	private final String id;
	private final String type;
	private final long timeStamp;

	private static final long serialVersionUID = 1L;

	public Event(String id, String type, long timeStamp) {
		super();
		this.id = id;
		this.type = type;
		this.timeStamp = timeStamp;
	}

	public Event(String type) {
		this("" + uid.getAndIncrement(), type, new Date().getTime());
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(getType()).append(": ")
				.append(new Date(getTimeStamp()).toString()).append(" - ")
				.toString();
	}

}
