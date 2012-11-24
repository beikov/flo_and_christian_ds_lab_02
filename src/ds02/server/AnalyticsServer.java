package ds02.server;

import ds02.server.service.AnalyticsService;
import ds02.server.service.impl.AnalyticsServiceImpl;
import ds02.server.util.RegistryUtils;
import ds02.server.util.RuntimeUtils;

public class AnalyticsServer {

	public static void main(String[] args) {
		try {
			RegistryUtils.bindService(AnalyticsService.class,
					AnalyticsServiceImpl.class);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		RuntimeUtils.waitForExitCommand();

	}
}
