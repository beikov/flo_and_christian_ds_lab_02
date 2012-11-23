package ds01.server.event.handler;

import ds01.server.UserConnection;
import ds01.server.event.AuctionEndedEvent;
import ds01.server.event.EventHandler;
import ds01.server.service.UserService;
import ds01.server.util.Formats;

public class AuctionEndedHandler implements EventHandler<AuctionEndedEvent> {
    
    @Override
    public void handle(AuctionEndedEvent event) {
        final UserConnection ownerUserConnection = UserService.INSTANCE.getUser(event.getAuction().getUser());
        final UserConnection bidUserConnection = event.getAuction().getBidUser() == null ? null : UserService.INSTANCE.getUser(event.getAuction().getBidUser());
        final StringBuilder sb = new StringBuilder();
        final String response;
            
        sb.append("!auction-ended ");
        if(event.getAuction().getBidUser() == null){
            sb.append("none");
        } else {
            sb.append(event.getAuction().getBidUser());
        }
        
        sb.append(' ');
        sb.append(Formats.getNumberFormat().format(event.getAuction().getBidValue()));
        sb.append(' ');
        sb.append(event.getAuction().getDescription());
        response = sb.toString();
        
        if(ownerUserConnection != null){
            ownerUserConnection.asyncResponse(response);
        }
        
        if(bidUserConnection != null && !bidUserConnection.equals(ownerUserConnection)){
            bidUserConnection.asyncResponse(response);
        }
    }
    
}
