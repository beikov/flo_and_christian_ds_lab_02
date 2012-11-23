package ds02.server.event.handler;

import ds02.server.UserConnection;
import ds02.server.event.EventHandler;
import ds02.server.event.NewBidEvent;
import ds02.server.service.UserService;

public class NewBidHandler implements EventHandler<NewBidEvent> {
    
    @Override
    public void handle(NewBidEvent event) {
        final UserConnection userConnection = UserService.INSTANCE.getUser(event.getUser());
        
        /* Not specified if a user should be notified if he overbid himself, so we notify him */
        if(userConnection != null){
            final StringBuilder sb = new StringBuilder();
            sb.append("!new-bid ").append(event.getAuction().getDescription());
            
//            userConnection.asyncResponse(sb.toString());
        }
    }
    
}
