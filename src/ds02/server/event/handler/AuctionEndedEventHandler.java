package ds02.server.event.handler;

import java.rmi.RemoteException;

import ds02.server.event.AuctionEndedEvent;
import ds02.server.event.BidWonEvent;
import ds02.server.model.Auction;
import ds02.server.service.AuctionService;
import ds02.server.service.BillingServiceSecure;
import ds02.server.service.ServiceLocator;

public class AuctionEndedEventHandler extends
		AbstractEventHandler<AuctionEndedEvent> {

	@Override
	public void handle(AuctionEndedEvent event) {
		// TODO Auto-generated method stub
		super.handle(event);
		Auction auction = AuctionService.INSTANCE.getAuction(event
				.getAuctionId());
		if (auction.getBidUser() != null) {
			DefaultEventHandler.INSTANCE.handle(new BidWonEvent(auction
					.getBidUser(), auction.getId(), auction.getBidValue()
					.doubleValue()));
		}
		try {
			BillingServiceSecure billingServiceSecure = ServiceLocator.INSTANCE
					.getBillingService().login("user", "password");

			billingServiceSecure.billAuction(auction.getBidUser(),
					event.getAuctionId(), auction.getBidValue().doubleValue());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
