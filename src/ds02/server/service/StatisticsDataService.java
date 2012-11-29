package ds02.server.service;

import java.util.Date;

public interface StatisticsDataService {

	public Date getServerStartTime();

	public long getTotalSessionTime();

	public long getTotalAuctionTime();

	public long getTotalBidCount();

	public double getBidCountPerMinute();

	public long getMaxUserSessionTime();

	public long getMinUserSessionTime();

	public double getAverageUserSessionTime();

	public long getTotalSuccessfulAuctionCount();

	public long getTotalAuctionCount();

	public double getAuctionSuccessRatio();

	public void addAuctionDuration(long auctionDuration);

	public void addUserSessionTime(long sessionTime);

	public void incrementBidCount();

	public void incrementSessionCount();

	public void offerHighestBid(double bidAmount);

	public double getHighestBid();

	public void incrementSuccessfulAuctionCount();

	public double getAverageAuctionTime();

	void incrementAuctionCount();
}
