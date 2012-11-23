package ds02.server.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import ds02.server.model.Bill;
import ds02.server.model.PriceSteps;

public interface BillingServiceSecure extends Remote, Serializable {
	public PriceSteps getPriceSetps() throws RemoteException;

	public void createPriceStep(double startPrice, double endPrice,
			double fixedPrice, double variablePricePercent)
			throws RemoteException;

	public void deletePriceStep(double startPrice, double endPrice)
			throws RemoteException;

	public String billAuction(String user, long auctionId, double price)
			throws RemoteException;

	public Bill getBill(String user) throws RemoteException;

}
