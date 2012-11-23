package ds01.server.service;

import ds01.server.event.AuctionEndedEvent;
import ds01.server.event.EventHandler;
import ds01.server.event.NewBidEvent;
import ds01.server.model.Auction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BidService {

    public static final BidService INSTANCE = new BidService();
    private final AtomicInteger currentId = new AtomicInteger(0);
    private final ConcurrentMap<Integer, AuctionEntry> auctions = new ConcurrentHashMap<Integer, AuctionEntry>();
    private transient EventHandler<NewBidEvent> overbidHandler = null;
    private transient EventHandler<AuctionEndedEvent> auctionEndHandler = null;

    private BidService() {
    }

    public EventHandler<NewBidEvent> getOverbidHandler() {
        return overbidHandler;
    }

    public void setOverbidHandler(EventHandler<NewBidEvent> overbidHandler) {
        this.overbidHandler = overbidHandler;
    }

    public EventHandler<AuctionEndedEvent> getAuctionEndHandler() {
        return auctionEndHandler;
    }

    public void setAuctionEndHandler(EventHandler<AuctionEndedEvent> auctionEndHandler) {
        this.auctionEndHandler = auctionEndHandler;
    }

    public void cancelAuctions() {
        final Iterator<Map.Entry<Integer, AuctionEntry>> iter = auctions.entrySet().iterator();

        while (iter.hasNext()) {
            iter.next().getValue().timer.cancel();
            iter.remove();
        }
    }

    public List<Auction> getAuctions() {
        final List<Auction> list = new ArrayList<Auction>();

        /* Clone the data so that for thread safety */
        for (AuctionEntry auction : auctions.values()) {
            list.add(auction.auction.clone());
        }

        return list;
    }

    public Auction createAuction(String user, Integer duration, String description) {
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
        final int id = currentId.incrementAndGet();

        final Auction auction = new Auction(id, description, user, end);
        final AuctionEntry entry = new AuctionEntry(auction, new Timer(true));

        /* Schedule a handler for notification */
        entry.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final EventHandler<AuctionEndedEvent> handler = auctionEndHandler;

                if (handler != null) {
                    final AuctionEntry auctionEntry = auctions.remove(id);
                    
                    if(auctionEntry != null){
                        final Auction auction = auctionEntry.auction;

                        if(auction != null){
                            synchronized(auction){
                                handler.handle(new AuctionEndedEvent(auction));
                            }
                        }
                    }
                }
            }
        }, end.getTime());

        auctions.put(id, entry);

        return auction;
    }

    public Auction bid(String user, Integer id, BigDecimal amount) {
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("Invalid user");
        }
        
        if (id == null) {
            throw new IllegalArgumentException("Invalid id");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("Invalid amount");
        }

        final AuctionEntry auctionEntry = auctions.get(id);
        
        if (auctionEntry == null) {
            throw new IllegalArgumentException("Auction for id " + id + " does not exist");
        }
        
        final Auction auction = auctionEntry.auction;

        if (auction == null) {
            throw new IllegalArgumentException("Auction for id " + id + " does not exist");
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

        if (overbidUser != null) {
            /* Notify the user who has been overbidden */
            final EventHandler<NewBidEvent> handler = overbidHandler;

            if (handler != null) {
                handler.handle(new NewBidEvent(overbidUser, result));
            }
        }

        return result;
    }

    private static class AuctionEntry {

        final Auction auction;
        final Timer timer;

        public AuctionEntry(Auction auction, Timer timer) {
            this.auction = auction;
            this.timer = timer;
        }
    }
}
