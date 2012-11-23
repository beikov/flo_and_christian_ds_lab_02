package ds02.server.event;

public class StatisticsEvent extends Event {
	private final double value;

	public StatisticsEvent(String id, String type, long timeStamp, double value) {
		super(id, type, timeStamp);
		this.value = value;
	}

	public StatisticsEvent(String type, double value) {
		super(type);
		this.value = value;
	}

	public double getValue() {
		return value;
	}
	
	
}
