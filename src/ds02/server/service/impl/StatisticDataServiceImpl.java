package ds02.server.service.impl;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import ds02.server.service.StatisticsDataService;
import ds02.server.util.concurrent.AtomicDouble;

public class StatisticDataServiceImpl implements StatisticsDataService {
	private final Date startTime;
	private final AtomicLong totalSessionTime = new AtomicLong();
	private final AtomicLong totalSessionCount = new AtomicLong();
	private final AtomicLong totalAuctionTime = new AtomicLong();
	private final AtomicLong totalSuccessfullAuctions = new AtomicLong();
	private final AtomicLong totalAuctionCount = new AtomicLong();
	private final AtomicLong totalBidCount = new AtomicLong();
	private final AtomicDouble highestBid = new AtomicDouble();
	private final AtomicLong minUserTime = new AtomicLong();
	private final AtomicLong maxUserTime = new AtomicLong();

	public static StatisticDataServiceImpl INSTANCE = new StatisticDataServiceImpl();

	private StatisticDataServiceImpl() {
		startTime = new Date();
	}

	@Override
	public Date getServerStartTime() {
		return startTime;
	}

	@Override
	public long getOverallSessionTime() {
		return totalSessionTime.get();
	}

	@Override
	public long getOverallAuctionTime() {
		return totalAuctionTime.get();
	}

	@Override
	public double getMaxBidPrice() {
		return highestBid.get();
	}

	@Override
	public long getTotalBidCount() {
		return totalBidCount.get();
	}

	@Override
	public double getBidCountPerMinute() {
		return (totalBidCount.get() / ((new Date().getTime() - startTime
				.getTime()) / 60000));
	}

	@Override
	public long getLongestUserSessionTime() {
		return maxUserTime.get();
	}

	@Override
	public long getMinUserSessionTime() {
		return minUserTime.get();
	}

	@Override
	public double getAverageUserSessionTime() {
		return ((double) totalSessionTime.get() / (double) totalSessionCount
				.get());
	}

	@Override
	public long getOverallNumberOfSuccessfullyFinishedAuctions() {
		return totalSuccessfullAuctions.get();
	}

	@Override
	public double getAuctionSuccessRatio() {
		return ((double) totalSuccessfullAuctions.get() / (double) totalAuctionCount
				.get());
	}

	@Override
	public long getOverallNumberOfFinishedAuction() {
		return totalAuctionCount.get();
	}

	@Override
	public void addBid(double bidAmount) {
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
	public long getOverallTimeOfUserSessions() {
		return totalSessionTime.get();
	}

	@Override
	public double getAverageAuctionTime() {
		return ((double) totalAuctionTime.get() / (double) totalAuctionCount
				.get());
	}

	@Override
	public void addFinishedAuction(long auctionDuration) {
		totalAuctionTime.addAndGet(auctionDuration);
	}

	@Override
	public void incrementSuccessfullAuctions() {
		totalSuccessfullAuctions.incrementAndGet();
	}

	@Override
	public void incrementAuctionCount() {
		totalAuctionCount.incrementAndGet();
	}

	@Override
	public void addUserSessionTime(long sessionTime) {
		// lock free :-)
		while (true) {
			long minUTime = minUserTime.get();
			if (sessionTime < minUTime) {
				if (minUserTime.compareAndSet(minUTime, sessionTime)) {
					break;
				}

			} else {
				break;
			}
		}
		while (true) {
			long maxUTime = maxUserTime.get();
			if (sessionTime > maxUTime) {
				if (maxUserTime.compareAndSet(maxUTime, sessionTime)) {
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
