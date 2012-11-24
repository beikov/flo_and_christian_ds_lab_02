package ds02.server.service;

import java.rmi.server.UnicastRemoteObject;

import ds02.server.util.RegistryUtils;

public final class ServiceLocator {

	public static ServiceLocator INSTANCE = new ServiceLocator();

	private final Object billingLock = new Object();
	private final Object analyticsLock = new Object();

	private volatile BillingService billingService;
	private volatile AnalyticsService analyticsService;

	public BillingService getBillingService() {
		if (billingService == null) {
			synchronized (billingLock) {
				if (billingService == null) {
					billingService = RegistryUtils
							.getRemote(BillingService.class);
				}
			}
		}
		return billingService;
	}

	public AnalyticsService getAnalyticsService() {
		if (analyticsService == null) {
			synchronized (analyticsLock) {
				if (analyticsService == null) {
					analyticsService = RegistryUtils
							.getRemote(AnalyticsService.class);
				}
			}
		}
		return analyticsService;
	}

}
