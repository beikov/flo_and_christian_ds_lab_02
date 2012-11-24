package ds02.server.service;

import java.util.Date;

public interface StatisticsDataService {

	public Date getServerStartTime();

	public long getOverallSessionTime();

	public long getOverallAuctionTime();

	public long getTotalBidCount();

	public double getBidCountPerMinute();

	public long getLongestUserSessionTime();

	public long getMinUserSessionTime();

	public double getAverageUserSessionTime();

	public long getOverallNumberOfSuccessfullyFinishedAuctions();

	public long getOverallNumberOfFinishedAuction();

	public long getOverallTimeOfUserSessions();

	public double getAuctionSuccessRatio();

	public void addFinishedAuction(long auctionDuration);

	public void addUserSessionTime(long sessionTime);

	public void incrementBidCount();

	public void incrementSessionCount();

	public void addBid(double bidAmount);

	public double getMaxBidPrice();

	public void incrementSuccessfullAuctions();

	public double getAverageAuctionTime();

	void incrementAuctionCount();
}
