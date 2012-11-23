package ds02.server;

import ds02.server.service.AnalyticsService;
import ds02.server.service.impl.AnalyticsServiceImpl;
import ds02.server.util.ReaderUtils;
import ds02.server.util.RegistryUtils;

public class AnalyticsServer {
	
	public static void main(String[] args) {
		RegistryUtils.bindService(AnalyticsService.class, new AnalyticsServiceImpl());
		
		ReaderUtils.waitForExitCommand();

	}
}
