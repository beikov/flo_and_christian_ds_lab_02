package ds02.server.util.concurrent;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicDouble {
	private final AtomicLong longValue;

	public AtomicDouble() {
		longValue = new AtomicLong();
	}

	public AtomicDouble(double value) {
		longValue = new AtomicLong(Double.doubleToRawLongBits(value));
	}

	public final double get() {
		return Double.longBitsToDouble(longValue.get());
	}

	public boolean compareAndSet(double original, double newValue) {
		long current = Double.doubleToRawLongBits(original);
		long next = Double.doubleToRawLongBits(newValue);
		return (longValue.compareAndSet(current, next));
	}

}
