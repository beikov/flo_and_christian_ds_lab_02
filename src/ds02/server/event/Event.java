package ds02.server.event;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class Event implements Serializable {
	private final String id;
	private final String type;
	private final long timeStamp;
	
	public Event(String id, String type, long timeStamp) {
		super();
		this.id = id;
		this.type = type;
		this.timeStamp = timeStamp;
	}
	
	public Event(String type) {
		this(UUID.randomUUID().toString(), type, new Date().getTime());
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
	
	
	
}