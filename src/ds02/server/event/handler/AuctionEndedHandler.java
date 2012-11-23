package ds02.server.event.handler;

import ds02.server.UserConnection;
import ds02.server.event.AuctionEndedEvent;
import ds02.server.event.EventHandler;
import ds02.server.model.Auction;
import ds02.server.service.BidService;
import ds02.server.service.UserService;
import ds02.server.util.Formats;

public class AuctionEndedHandler implements EventHandler<AuctionEndedEvent> {
    
    @Override
    public void handle(AuctionEndedEvent event) {
    	final Auction auction = BidService.INSTANCE.getAuction(event.getAuctionId());
    	
        final UserConnection ownerUserConnection = UserService.INSTANCE.getUser(auction.getUser());
        final UserConnection bidUserConnection = auction.getBidUser() == null ? null : UserService.INSTANCE.getUser(auction.getBidUser());
        final StringBuilder sb = new StringBuilder();
        final String response;
            
        sb.append("!auction-ended ");
        if(auction.getBidUser() == null){
            sb.append("none");
        } else {
            sb.append(auction.getBidUser());
        }
        
        sb.append(' ');
        sb.append(Formats.getNumberFormat().format(auction.getBidValue()));
        sb.append(' ');
        sb.append(auction.getDescription());
        response = sb.toString();
        
//        if(ownerUserConnection != null){
//            ownerUserConnection.asyncResponse(response);
//        }
//        
//        if(bidUserConnection != null && !bidUserConnection.equals(ownerUserConnection)){
//            bidUserConnection.asyncResponse(response);
//        }
    }
    
}
