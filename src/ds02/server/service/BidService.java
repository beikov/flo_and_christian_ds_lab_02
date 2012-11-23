package ds02.server.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import ds02.server.model.Auction;
import ds02.server.service.impl.BidServiceImpl;
import ds02.server.util.AuctionRemoveTask;

public interface BidService extends Serializable {

	public static final BidService INSTANCE = new BidServiceImpl();
	public static final AuctionRemoveTask REMOVE_TASK = new AuctionRemoveTask();

	public List<Auction> getAuctions();

	public Auction getAuction(long auctionId);

	public Auction createAuction(String user, Integer duration,
			String description);

	public Auction bid(String user, Integer id, BigDecimal amount);

}