package ds02.server.event.handler;

import java.rmi.RemoteException;

import ds02.server.event.AuctionEndedEvent;
import ds02.server.model.Auction;
import ds02.server.service.BidService;
import ds02.server.service.BillingServiceSecure;
import ds02.server.service.ServiceLocator;

public class AuctionEndedEventHandler extends
		AbstractEventHandler<AuctionEndedEvent> {

	@Override
	public void handle(AuctionEndedEvent event) {
		// TODO Auto-generated method stub
		super.handle(event);
		
		try {
			BillingServiceSecure billingServiceSecure = ServiceLocator.INSTANCE.getBillingService().login(
					"user", "password");
			Auction auction = BidService.INSTANCE.getAuction(event
					.getAuctionId());
			
			billingServiceSecure.billAuction(auction.getBidUser(),
					event.getAuctionId(), auction.getBidValue().doubleValue());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
