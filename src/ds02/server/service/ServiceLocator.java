package ds02.server.service;

import java.rmi.RemoteException;

import ds02.server.util.Pingable;
import ds02.server.util.RegistryUtils;

public final class ServiceLocator {

	private static String analyticsServiceName = AnalyticsService.class
			.getName();
	private static String billingServiceName = BillingService.class.getName();
	public static ServiceLocator INSTANCE = new ServiceLocator();

	private final Object billingLock = new Object();
	private final Object analyticsLock = new Object();

	private volatile BillingService billingService;
	private volatile AnalyticsService analyticsService;

	public static void init(String analyticsServiceName,
			String billingServiceName) {
		if (analyticsServiceName != null) {
			ServiceLocator.analyticsServiceName = analyticsServiceName;
		}
		if (billingServiceName != null) {
			ServiceLocator.billingServiceName = billingServiceName;
		}
	}

	public BillingService getBillingService() {
		if (billingService == null || notConnected(billingService)) {
			synchronized (billingLock) {
				if (billingService == null || notConnected(billingService)) {
					billingService = RegistryUtils
							.getRemote(billingServiceName);
				}
			}
		}
		return billingService;
	}

	public AnalyticsService getAnalyticsService() {
		if (analyticsService == null || notConnected(analyticsService)) {
			synchronized (analyticsLock) {
				if (analyticsService == null || notConnected(analyticsService)) {
					analyticsService = RegistryUtils
							.getRemote(analyticsServiceName);
				}
			}
		}
		return analyticsService;
	}

	private boolean notConnected(Pingable p) {
		try {
			p.ping();
			return true;
		} catch (RemoteException e) {
			return false;
		}
	}

}
