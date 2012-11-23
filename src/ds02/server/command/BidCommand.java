package ds02.server.command;

import ds02.server.UserConnection;
import ds02.server.model.Auction;
import ds02.server.service.BidService;
import ds02.server.util.Formats;
import java.math.BigDecimal;

public class BidCommand implements Command{
    
    private final BidService bidService;

    public BidCommand(BidService bidService) {
        this.bidService = bidService;
    }
    
    @Override
    public void execute(UserConnection userConnection, String[] args){
        Integer id = null;
        BigDecimal amount = null;
        
        if(args.length > 0){
            try{
                id = Integer.parseInt(args[0]);
            } catch(NumberFormatException ex){
                /* Service will throw an exception with a good message */
            }
        }
        
        if(args.length > 1){
            try{
                amount = new BigDecimal(args[1]);
            } catch(NumberFormatException ex){
                /* Service will throw an exception with a good message */
            }
        }
        
        final Auction auction = bidService.bid(userConnection.getUsername(), id, amount);
        final boolean success = auction.getBidValue().compareTo(amount) < 0;
        final StringBuilder sb = new StringBuilder();
        
        sb.append("You ");
        
        if(success){
            sb.append("successfully");
        } else {
            sb.append("unsuccessfully");
        }
        
        sb.append(" bid with ");
        sb.append(amount);
        sb.append(" on '");
        sb.append(auction.getDescription());
        sb.append("'");
        
        if(!success){
            sb.append(". Current highest bid is ");
            sb.append(Formats.getNumberFormat().format(auction.getBidValue()));
        }
        
        sb.append('.');
        
        userConnection.writeResponse(sb.toString());
    }
}