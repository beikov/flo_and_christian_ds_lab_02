package ds02.server.service;

import java.util.Date;

import ds02.server.model.Auction;

public interface StatisticsDataService {

		public Date getServerStartTime();
		
		public long getOverallSessionTime();
		
		public long getOverallAuctionTime();
		
		public double getOverallBidPrice();
		
		public long getTotalBidCount();
		
		public double getBidCountPerMinute();
		
		public long getLongestUserSessionTime();
		
		public long getShortestUserSessionTime();
		
		public double getAverageUserSessionTime();
		
		public long getOverallNumberOfSuccessfullyFinishedAuctions();
		
		public long getOverallNumberOfFinishedAuction();
		
		public long getOverallTimeOfUserSessions();
		
		public double getAuctionSuccessRatio();
		
		public void addFinishedAuction(Auction auction);
		
		public void addUserSessionTime(long sessionTime);
		
		public void incrementBidCount();
	
		public void incrementSessionCount();
}
