package ds01.server.event;

import ds01.server.model.Auction;

public class NewBidEvent extends UserEvent{
    
    private final Auction auction;
    
    public NewBidEvent(String user, Auction auction) {
        super(user);
        this.auction = auction;
    }

    public Auction getAuction() {
        return auction;
    }
}
