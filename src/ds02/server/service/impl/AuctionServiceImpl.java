package ds02.server.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import ds02.server.event.AuctionEndedEvent;
import ds02.server.event.AuctionStartedEvent;
import ds02.server.event.BidOverbidEvent;
import ds02.server.event.BidPlacedEvent;
import ds02.server.event.Event;
import ds02.server.event.EventHandler;
import ds02.server.event.handler.AuctionEndedEventHandler;
import ds02.server.event.handler.DefaultEventHandler;
import ds02.server.model.Auction;
import ds02.server.service.AuctionService;
import ds02.server.util.TimedTask;

public class AuctionServiceImpl implements AuctionService {

	private static final long serialVersionUID = 1L;
	private final AtomicLong currentId = new AtomicLong(0);
	private final ConcurrentMap<Long, Auction> auctions = new ConcurrentHashMap<Long, Auction>();
	private transient EventHandler<Event> overbidHandler = DefaultEventHandler.INSTANCE;
	private transient EventHandler<AuctionEndedEvent> auctionEndHandler = new AuctionEndedEventHandler();

	/*
	 * (non-Javadoc)
	 * 
	 * @see ds02.server.service.impl.BidService#getAuctions()
	 */
	@Override
	public List<Auction> getAuctions() {
		final List<Auction> list = new ArrayList<Auction>();

		/* Clone the data so that for thread safety */
		for (Auction auction : auctions.values()) {
			list.add(auction.clone());
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ds02.server.service.impl.BidService#getAuction(long)
	 */
	@Override
	public Auction getAuction(long auctionId) {
		final Auction auction = auctions.get(auctionId);
		if (auction != null) {
			return auction.clone();
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ds02.server.service.impl.BidService#createAuction(java.lang.String,
	 * java.lang.Integer, java.lang.String)
	 */
	@Override
	public Auction createAuction(String user, Integer duration,
			String description) {
		if (user == null || user.isEmpty()) {
			throw new IllegalArgumentException("Invalid user");
		}

		if (duration == null || duration < 1) {
			throw new IllegalArgumentException("Invalid duration");
		}

		if (description == null || description.isEmpty()) {
			throw new IllegalArgumentException("Invalid description");
		}

		final Calendar end = Calendar.getInstance();
		end.add(Calendar.SECOND, duration);
		final long id = currentId.incrementAndGet();

		final Auction auction = new Auction(id, description, user, end);

		/* Schedule a handler for notification */
		REMOVE_TASK.add(new TimedTask(end.getTime()) {
			@Override
			public void run() {
				final EventHandler<AuctionEndedEvent> handler = auctionEndHandler;

				if (handler != null) {
					final Auction auction = auctions.get(id);

					if (auction != null) {
						synchronized (auction) {
							handler.handle(new AuctionEndedEvent(id));
							auctions.remove(id);
						}

					}
				}
			}
		});

		auctions.put(id, auction);
		DefaultEventHandler.INSTANCE.handle(new AuctionStartedEvent(id));
		return auction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ds02.server.service.impl.BidService#bid(java.lang.String,
	 * java.lang.Integer, java.math.BigDecimal)
	 */
	@Override
	public Auction bid(String user, Long id, BigDecimal amount) {
		if (user == null || user.isEmpty()) {
			throw new IllegalArgumentException("Invalid user");
		}

		if (id == null) {
			throw new IllegalArgumentException("Invalid id");
		}

		if (amount == null || amount.compareTo(BigDecimal.ZERO) < 1) {
			throw new IllegalArgumentException("Invalid amount");
		}

		final Auction auction = auctions.get(id);

		if (auction == null) {
			throw new IllegalArgumentException("Auction for id " + id
					+ " does not exist");
		}

		/* Not specified if a user may bid on his own auction, so we allow it */

		final Auction result;
		String overbidUser = null;

		synchronized (auction) {
			result = auction.clone();

			/* Avoid partially updated auction, also see {@link Auction#clone} */
			if (amount.compareTo(auction.getBidValue()) > 0) {
				overbidUser = auction.getBidUser();
				auction.setBidUser(user);
				auction.setBidValue(amount);
			}
		}

		DefaultEventHandler.INSTANCE.handle(new BidPlacedEvent(user, id, amount
				.doubleValue()));

		if (overbidUser != null) {
			/* Notify the user who has been overbidden */
			final EventHandler<Event> handler = overbidHandler;

			if (handler != null) {
				handler.handle(new BidOverbidEvent(overbidUser, result.getId(),
						amount.doubleValue()));
			}
		}

		return result;
	}
}
