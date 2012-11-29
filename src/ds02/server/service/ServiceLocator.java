package ds02.server.service;

import java.lang.reflect.Proxy;

import ds02.server.util.ReconnectInterceptor;

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
		if (billingService == null) {
			synchronized (billingLock) {
				if (billingService == null) {
					billingService = (BillingService) Proxy.newProxyInstance(
							BillingService.class.getClassLoader(),
							new Class[] { BillingService.class },
							new ReconnectInterceptor(billingServiceName));
				}
			}
		}
		return billingService;
	}

	public AnalyticsService getAnalyticsService() {
		if (analyticsService == null) {
			synchronized (analyticsLock) {
				if (analyticsService == null) {
					analyticsService = (AnalyticsService) Proxy.newProxyInstance(
							AnalyticsService.class.getClassLoader(),
							new Class[] { AnalyticsService.class },
							new ReconnectInterceptor(analyticsServiceName));
				}
			}
		}
		return analyticsService;
	}

}
