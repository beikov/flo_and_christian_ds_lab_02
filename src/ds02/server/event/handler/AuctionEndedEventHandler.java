package ds02.server.event.handler;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import ds02.server.event.AuctionEndedEvent;
import ds02.server.event.BidWonEvent;
import ds02.server.model.Auction;
import ds02.server.service.AuctionService;
import ds02.server.service.BillingServiceSecure;
import ds02.server.service.ServiceLocator;

public class AuctionEndedEventHandler extends
		AbstractEventHandler<AuctionEndedEvent> {

	private static final Logger LOG = Logger
			.getLogger(AuctionEndedEventHandler.class);

	@Override
	public void handle(AuctionEndedEvent event) {
		Auction auction = AuctionService.INSTANCE.getAuction(event
				.getAuctionId());

		if (auction.getBidUser() != null) {
			DefaultEventHandler.INSTANCE.handle(new BidWonEvent(auction
					.getBidUser(), auction.getId(), auction.getBidValue()
					.doubleValue()));
		}
		
		super.handle(event);

		try {
			BillingServiceSecure billingServiceSecure = ServiceLocator.INSTANCE
					.getBillingService().login("john", "dslab2012");

			billingServiceSecure.billAuction(auction.getUser(),
					event.getAuctionId(), auction.getBidValue().doubleValue());
		} catch (RemoteException e) {
			LOG.warn("BillingService is not reachable!");
		}

	}

}
