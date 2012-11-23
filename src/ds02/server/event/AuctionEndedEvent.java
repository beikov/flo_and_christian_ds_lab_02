package ds02.server.event;

import ds02.server.model.Auction;

public class AuctionEndedEvent {
    
    private final Auction auction;
    
    public AuctionEndedEvent(Auction auction) {
        this.auction = auction;
    }

    public Auction getAuction() {
        return auction;
    }
}
