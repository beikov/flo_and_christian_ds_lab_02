package ds02.server.event.handler;

import ds02.server.UserConnection;
import ds02.server.event.BidOverbidEvent;
import ds02.server.event.EventHandler;
import ds02.server.model.Auction;
import ds02.server.service.BidService;
import ds02.server.service.UserService;

public class BidOverBidHandler implements EventHandler<BidOverbidEvent> {
    
    @Override
    public void handle(BidOverbidEvent event) {
    	final Auction auction = BidService.INSTANCE.getAuction(event.getAuctionId());
        final UserConnection userConnection = UserService.INSTANCE.getUser(event.getUsername());
        
        /* Not specified if a user should be notified if he overbid himself, so we notify him */
        if(userConnection != null){
            final StringBuilder sb = new StringBuilder();
            sb.append("!new-bid ").append(auction.getDescription());
            
//            userConnection.asyncResponse(sb.toString());
        }
    }
    
}
