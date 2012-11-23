package ds02.server.service.impl;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import ds02.server.model.Auction;
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
	private final AtomicDouble totalBidPrice = new AtomicDouble();

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
	public double getOverallBidPrice() {
		return totalBidPrice.get();
	}

	@Override
	public long getTotalBidCount() {
		return totalBidCount.get();
	}

	@Override
	public double getBidCountPerMinute() {
		return (totalBidCount.get() / (new Date().getTime() - startTime
				.getTime()));
	}

	@Override
	public long getLongestUserSessionTime() {
		return maxUserTime.get();
	}

	@Override
	public long getShortestUserSessionTime() {
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
		return ((double) totalSuccessfullAuctions.get() / (double) totalAuctionCount.get());
	}
	@Override
	public long getOverallNumberOfFinishedAuction() {
		return totalAuctionCount.get();
	}

	@Override
	public long getOverallTimeOfUserSessions() {
		return totalSessionTime.get();
	}

	@Override
	public void addFinishedAuction(Auction auction) {
		totalAuctionTime.addAndGet(auction.getEndTimestamp().getTimeInMillis()
				- auction.getEndTimestamp().getTimeInMillis());
		totalBidPrice.getAndAdd(auction.getBidValue().doubleValue());
		totalAuctionCount.incrementAndGet();
		if (auction.getBidValue().doubleValue() > 0) {
			totalSuccessfullAuctions.incrementAndGet();
		}
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

			}
		}
		while (true) {
			long maxUTime = minUserTime.get();
			if (sessionTime > maxUTime) {
				if (maxUserTime.compareAndSet(maxUTime, sessionTime)) {
					break;
				}

			}
		}
		totalSessionTime.addAndGet(sessionTime);
	}

	@Override
	public void incrementBidCount() {
		totalBidCount.incrementAndGet();
	}

	public void incrementSessionCount() {
		totalSessionCount.incrementAndGet();
	}

}