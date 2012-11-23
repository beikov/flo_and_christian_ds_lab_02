package ds02.server.util.concurrent;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicDouble {
	private final AtomicLong longValue;

	public AtomicDouble() {
		longValue = new AtomicLong();
	}
	
	public AtomicDouble(double value){
		longValue = new AtomicLong(Double.doubleToRawLongBits(value));
	}
	
	public final double get(){
		return Double.longBitsToDouble(longValue.get());
	}
	
	public double getAndAdd(double delta){
		while(true){
			long current = longValue.get();
			double currentVal = Double.longBitsToDouble(current);
			double nextVal = currentVal + delta;
			long next = Double.doubleToRawLongBits(nextVal);
			
			if (longValue.compareAndSet(current, next)){
				return nextVal;
			}
		}
	}
}
