package ds02.server;

import ds02.server.service.AnalyticsService;
import ds02.server.service.BillingService;
import ds02.server.service.impl.AnalyticsServiceImpl;
import ds02.server.service.impl.BillingServiceImpl;
import ds02.server.util.ReaderUtils;
import ds02.server.util.RegistryUtils;

public class BillingServer {
	
	public static void main(String[] args) {
		RegistryUtils.bindService(BillingService.class, new BillingServiceImpl());
		ReaderUtils.waitForExitCommand();

	}
}
