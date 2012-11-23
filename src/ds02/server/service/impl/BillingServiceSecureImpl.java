package ds02.server.service.impl;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import ds02.server.model.Bill;
import ds02.server.model.BillLine;
import ds02.server.model.PriceStep;
import ds02.server.model.PriceSteps;
import ds02.server.service.BillingServiceSecure;

public class BillingServiceSecureImpl implements BillingServiceSecure {

	private static final long serialVersionUID = 1L;
	private static final Object STUB = new Object();
	
	private final ConcurrentNavigableMap<PriceStep, Object> priceSteps = new ConcurrentSkipListMap<PriceStep, Object>();
	private final ConcurrentMap<String, Bill> bills = new ConcurrentHashMap<String, Bill>();

	@Override
	public PriceSteps getPriceSetps() throws RemoteException {
		return new PriceSteps(priceSteps.keySet());
	}

	@Override
	public void createPriceStep(double startPrice, double endPrice,
			double fixedPrice, double variablePricePercent)
			throws RemoteException {
		if(startPrice < 0){
			throw new RemoteException("Start price may not be negative");
		}
		if(endPrice < 0){
			throw new RemoteException("End price may not be negative");
		}
		if(fixedPrice < 0){
			throw new RemoteException("Fixed price may not be negative");
		}
		if(variablePricePercent < 0){
			throw new RemoteException("Variable price percent may not be negative");
		}
		if(startPrice > endPrice) {
			throw new RemoteException("Start price must be lower than end price");
		}
		
		final PriceStep step = new PriceStep(startPrice, endPrice, fixedPrice, variablePricePercent);
		
		try{
			/* The compare method will take care of checking for overlapping */
			priceSteps.put(step, STUB);
		}catch(IllegalArgumentException ex){
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public void deletePriceStep(double startPrice, double endPrice)
			throws RemoteException {
		if(startPrice < 0){
			throw new RemoteException("Start price may not be negative");
		}
		if(endPrice < 0){
			throw new RemoteException("End price may not be negative");
		}
		if(startPrice > endPrice) {
			throw new RemoteException("Start price must be lower than end price");
		}
		
		final PriceStep step = new PriceStep(startPrice, endPrice, 0, 0);
		
		if(priceSteps.remove(step) == null){
			throw new RemoteException("The specified interval does not match an existing price step interval");
		}
	}

	@Override
	public void billAuction(String user, long auctionId, double price)
			throws RemoteException {
		if(user == null || user.isEmpty()){
			throw new RemoteException("Invalid username");
		}
		if(price < 0){
			throw new RemoteException("Price may not be negative");
		}
		
		final PriceStep step = priceSteps.lowerEntry(new PriceStep(price, price, 0, 0)).getKey();
		final BillLine line = new BillLine(auctionId, price, step.getFixedPrice(), step.getVariablePricePercent() * price);
		Bill bill = bills.get(user);
		
		if(bill == null){
			final Bill tempBill = bills.putIfAbsent(user, new Bill());
			
			if(tempBill != null){
				bill = tempBill;
			}
		}
		
		bill.add(line);
	}

	@Override
	public Bill getBill(String user) throws RemoteException {
		return bills.get(user);
	}

}
