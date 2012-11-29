package ds02.server.service.impl;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import ds02.server.service.StatisticsDataService;
import ds02.server.util.concurrent.AtomicDouble;

public class StatisticDataServiceImpl implements StatisticsDataService {
	private final Date startTime;

	private final AtomicLong totalSessionTime = new AtomicLong();
	private final AtomicLong totalSessionCount = new AtomicLong();
	private final AtomicLong minUserSessionTime = new AtomicLong(Long.MAX_VALUE);
	private final AtomicLong maxUserSessionTime = new AtomicLong();

	private final AtomicLong totalAuctionTime = new AtomicLong();
	private final AtomicLong totalSuccessfulAuctionCount = new AtomicLong();
	private final AtomicLong totalAuctionCount = new AtomicLong();

	private final AtomicLong totalBidCount = new AtomicLong();
	private final AtomicDouble highestBid = new AtomicDouble();

	public static StatisticDataServiceImpl INSTANCE = new StatisticDataServiceImpl();

	private StatisticDataServiceImpl() {
		startTime = new Date();
	}

	@Override
	public Date getServerStartTime() {
		return startTime;
	}

	@Override
	public long getTotalSessionTime() {
		return totalSessionTime.get();
	}

	@Override
	public long getTotalAuctionTime() {
		return totalAuctionTime.get();
	}

	@Override
	public double getHighestBid() {
		return highestBid.get();
	}

	@Override
	public long getTotalBidCount() {
		return totalBidCount.get();
	}

	@Override
	public double getBidCountPerMinute() {
		return (totalBidCount.get() / ((double)(new Date().getTime() - startTime
				.getTime()) / 60000));
	}

	@Override
	public long getMaxUserSessionTime() {
		return maxUserSessionTime.get();
	}

	@Override
	public long getMinUserSessionTime() {
		return minUserSessionTime.get();
	}

	@Override
	public double getAverageUserSessionTime() {
		return ((double) totalSessionTime.get() / (double) totalSessionCount
				.get());
	}

	@Override
	public long getTotalSuccessfulAuctionCount() {
		return totalSuccessfulAuctionCount.get();
	}

	@Override
	public double getAuctionSuccessRatio() {
		return ((double) totalSuccessfulAuctionCount.get() / (double) totalAuctionCount
				.get());
	}

	@Override
	public long getTotalAuctionCount() {
		return totalAuctionCount.get();
	}

	@Override
	public void offerHighestBid(double bidAmount) {
		while (true) {
			double maxBid = highestBid.get();
			if (bidAmount > maxBid) {
				if (highestBid.compareAndSet(maxBid, bidAmount)) {
					break;
				}

			} else {
				break;
			}
		}
	}

	@Override
	public double getAverageAuctionTime() {
		return ((double) totalAuctionTime.get() / (double) totalAuctionCount
				.get());
	}

	@Override
	public void addAuctionDuration(long auctionDuration) {
		totalAuctionTime.addAndGet(auctionDuration);
	}

	@Override
	public void incrementSuccessfulAuctionCount() {
		totalSuccessfulAuctionCount.incrementAndGet();
	}

	@Override
	public void incrementAuctionCount() {
		totalAuctionCount.incrementAndGet();
	}

	@Override
	public void addUserSessionTime(long sessionTime) {
		// lock free :-)
		while (true) {
			long minUTime = minUserSessionTime.get();
			if (sessionTime < minUTime) {
				if (minUserSessionTime.compareAndSet(minUTime, sessionTime)) {
					break;
				}

			} else {
				break;
			}
		}
		while (true) {
			long maxUTime = maxUserSessionTime.get();
			if (sessionTime > maxUTime) {
				if (maxUserSessionTime.compareAndSet(maxUTime, sessionTime)) {
					break;
				}

			} else {
				break;
			}
		}
		totalSessionTime.addAndGet(sessionTime);
	}

	@Override
	public void incrementBidCount() {
		totalBidCount.incrementAndGet();
	}

	@Override
	public void incrementSessionCount() {
		totalSessionCount.incrementAndGet();
	}

}
