package ds02.server.service.impl;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import ds02.server.event.AuctionEvent;
import ds02.server.event.AuctionSuccessRatioEvent;
import ds02.server.event.AuctionTimeAvgEvent;
import ds02.server.event.BidCountPerMinuteEvent;
import ds02.server.event.BidEvent;
import ds02.server.event.BidPriceMaxEvent;
import ds02.server.event.Event;
import ds02.server.event.EventCallback;
import ds02.server.event.EventHandler;
import ds02.server.event.UserEvent;
import ds02.server.event.UserSessiontimeAvgEvent;
import ds02.server.event.UserSessiontimeMaxEvent;
import ds02.server.event.UserSessiontimeMinEvent;
import ds02.server.service.AnalyticsService;

public class AnalyticsServiceImpl implements AnalyticsService {

	private static final long serialVersionUID = 1L;
	private final ConcurrentMap<String, EventHandler<Event>> subscrptions = new ConcurrentHashMap<String, EventHandler<Event>>();
	private final ConcurrentMap<String, Long> startValue = new ConcurrentHashMap<String, Long>();
	private final ConcurrentMap<Long, Long> auctionBegin = new ConcurrentHashMap<Long, Long>();

	public AnalyticsServiceImpl() {
		subscribe0("USER_LOGIN", new EventCallback() {

			@Override
			public void handle(Event event) {
				UserEvent userEvent = (UserEvent) event;
				startValue.put(userEvent.getUser(), userEvent.getTimeStamp());
				StatisticDataServiceImpl.INSTANCE.incrementSessionCount();

			}
		});
		subscribe0("(USER_LOGGOUT|USER_DISCONNECTED)", new EventCallback() {

			@Override
			public void handle(Event event) {
				UserEvent userEvent = (UserEvent) event;
				if (startValue.containsKey(userEvent.getUser())) {
					StatisticDataServiceImpl.INSTANCE
							.addUserSessionTime(startValue.get(userEvent
									.getUser()));

				}
				processEvent0(new UserSessiontimeMinEvent(
						StatisticDataServiceImpl.INSTANCE
								.getMinUserSessionTime()));
				processEvent0(new UserSessiontimeMaxEvent(
						StatisticDataServiceImpl.INSTANCE
								.getLongestUserSessionTime()));
				processEvent0(new UserSessiontimeAvgEvent(
						StatisticDataServiceImpl.INSTANCE
								.getAverageUserSessionTime()));
			}
		});
		subscribe0("USER_LOUGOUT", new EventCallback() {

			@Override
			public void handle(Event event) {
				StatisticDataServiceImpl.INSTANCE.incrementBidCount();
			}
		});

		subscribe0("AUCTION_STARTED", new EventCallback() {

			@Override
			public void handle(Event event) {
				auctionBegin.put(((AuctionEvent) event).getAuctionId(),
						new Date().getTime());
			}
		});
		
		subscribe0("BID_WON", new EventCallback() {

			@Override
			public void handle(Event event) {
				StatisticDataServiceImpl.INSTANCE.incrementSuccessfullAuctions();
				
				processEvent0(new AuctionSuccessRatioEvent(
						StatisticDataServiceImpl.INSTANCE.getAuctionSuccessRatio()));
			}
		});

		subscribe0("AUCTION_ENDED", new EventCallback() {

			@Override
			public void handle(Event event) {
				if (auctionBegin.containsKey(((AuctionEvent) event)
						.getAuctionId())) {
					StatisticDataServiceImpl.INSTANCE.addFinishedAuction(event
							.getTimeStamp()
							- auctionBegin.remove(((AuctionEvent) event)
									.getAuctionId()));
				}
				processEvent0(new AuctionTimeAvgEvent(
						StatisticDataServiceImpl.INSTANCE.getAverageAuctionTime()));
			}
		});

		subscribe0("BID_PLACED", new EventCallback() {

			@Override
			public void handle(Event event) {
				StatisticDataServiceImpl.INSTANCE.incrementBidCount();
				StatisticDataServiceImpl.INSTANCE.addBid(((BidEvent) event)
						.getPrice());

				processEvent0(new BidPriceMaxEvent(
						StatisticDataServiceImpl.INSTANCE.getMaxBidPrice()));
				processEvent0(new BidCountPerMinuteEvent(
						StatisticDataServiceImpl.INSTANCE
								.getBidCountPerMinute()));

			}
		});
	}

	@Override
	public String subscribe(final String pattern, final EventCallback handler)
			throws RemoteException {
		return subscribe0(pattern, handler);
	}

	private String subscribe0(final String pattern, final EventCallback handler) {

		final Pattern p = Pattern.compile(pattern);

		String uuid = null;
		EventHandler<Event> wrappedHandler = new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (p.matcher(event.getType()).matches()) {
					handler.handle(event);
				}
			}

		};

		do {
			uuid = UUID.randomUUID().toString();
		} while (subscrptions.putIfAbsent(uuid, wrappedHandler) != null);

		return uuid;
	}

	@Override
	public void processEvent(Event event) throws RemoteException {
		processEvent0(event);
	}

	private void processEvent0(Event event) {
		Iterator<EventHandler<Event>> it = subscrptions.values().iterator();

		while (it.hasNext()) {
			final EventHandler<Event> eventHandler = it.next();
			eventHandler.handle(event);
		}
	}

	@Override
	public void unsubscribe(String identifier) throws RemoteException {
		subscrptions.remove(identifier);
	}

}
