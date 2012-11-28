package ds02.server.service.impl;

import java.rmi.RemoteException;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ds02.server.exception.PriceStepException;
import ds02.server.model.Bill;
import ds02.server.model.BillLine;
import ds02.server.model.PriceStep;
import ds02.server.model.PriceSteps;
import ds02.server.service.BillingServiceSecure;

public class BillingServiceSecureImpl implements BillingServiceSecure {

	private static final long serialVersionUID = 1L;

	private final NavigableSet<PriceStep> priceSteps = new TreeSet<PriceStep>();
	private final ConcurrentMap<String, Bill> bills = new ConcurrentHashMap<String, Bill>();
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public PriceSteps getPriceSetps() throws RemoteException {
		lock.readLock().lock();

		try {
			return new PriceSteps(priceSteps);
		} finally {
			lock.readLock().unlock();
		}

	}

	@Override
	public void createPriceStep(double startPrice, double endPrice,
			double fixedPrice, double variablePricePercent)
			throws RemoteException, PriceStepException {
		if (startPrice < 0) {
			throw new PriceStepException("Start price may not be negative");
		}
		if (endPrice < 0) {
			throw new PriceStepException("End price may not be negative");
		}
		if (fixedPrice < 0) {
			throw new PriceStepException("Fixed price may not be negative");
		}
		if (variablePricePercent < 0) {
			throw new PriceStepException(
					"Variable price percent may not be negative");
		}
		if (startPrice > endPrice) {
			throw new PriceStepException(
					"Start price must be lower than end price");
		}

		final PriceStep step = new PriceStep(startPrice, endPrice, fixedPrice,
				variablePricePercent);

		lock.writeLock().lock();

		try {
			final PriceStep lower = priceSteps.lower(step);
			final PriceStep higher = priceSteps.higher(step);

			if (lower != null && lower.getEndPrice() > step.getStartPrice()) {
				throw new PriceStepException("Overlapping price steps");
			}
			if (higher != null && higher.getStartPrice() < step.getEndPrice()) {
				throw new PriceStepException("Overlapping price steps");
			}

			priceSteps.add(step);
		} catch (IllegalArgumentException ex) {
			throw new RemoteException(ex.getMessage());

		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void deletePriceStep(double startPrice, double endPrice)
			throws RemoteException, PriceStepException {
		if (startPrice < 0) {
			throw new PriceStepException("Start price may not be negative");
		}
		if (endPrice < 0) {
			throw new PriceStepException("End price may not be negative");
		}
		if (startPrice > endPrice) {
			throw new PriceStepException(
					"Start price must be lower than end price");
		}

		final PriceStep step = new PriceStep(startPrice, endPrice, 0, 0);

		lock.writeLock().lock();
		try {
			if (!priceSteps.remove(step)) {
				throw new PriceStepException(
						"The specified interval does not match an existing price step interval");
			}
		} finally {
			lock.writeLock().unlock();
		}

	}

	@Override
	public void billAuction(String user, long auctionId, double price)
			throws RemoteException {
		if (user == null || user.isEmpty()) {
			throw new RemoteException("Invalid username");
		}
		if (price < 0) {
			throw new RemoteException("Price may not be negative");
		}

		final PriceStep step;

		lock.readLock().lock();

		try {
			step = priceSteps.floor(new PriceStep(price, price, 0, 0));
		} finally {
			lock.readLock().unlock();
		}

		final BillLine line;

		if (step == null) {
			line = new BillLine(auctionId, price, 0, 0);
		} else {
			line = new BillLine(auctionId, price, step.getFixedPrice(),
					(step.getVariablePricePercent() / 100.0) * price);
		}

		Bill bill = bills.get(user);

		if (bill == null) {
			bill = new Bill();
			final Bill tempBill = bills.putIfAbsent(user, bill);

			if (tempBill != null) {
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
