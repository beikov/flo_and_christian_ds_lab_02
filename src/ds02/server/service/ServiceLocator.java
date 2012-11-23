package ds02.server.service;

import java.rmi.server.UnicastRemoteObject;

import ds02.server.util.RegistryUtils;

public final class ServiceLocator {

	public static ServiceLocator INSTANCE = new ServiceLocator();

	private final Object billingLock = new Object();
	private final Object analyticsLock = new Object();

	private volatile BillingService billingService;
	private volatile AnalyticsService analyticsService;

	private ServiceLocator() {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				if (INSTANCE.billingService != null) {
					synchronized (INSTANCE.billingLock) {
						if (INSTANCE.billingService != null) {
							try {
								UnicastRemoteObject.unexportObject(
										INSTANCE.billingService, true);
							} catch (Exception e) {
								// ignore error
							}
						}
					}
				}

				if (INSTANCE.analyticsService != null) {
					synchronized (INSTANCE.analyticsLock) {
						if (INSTANCE.analyticsService != null) {
							try {
								UnicastRemoteObject.unexportObject(
										INSTANCE.analyticsService, true);
							} catch (Exception e) {
								// ignore error
							}
						}
					}
				}
			}

		});
	}

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
