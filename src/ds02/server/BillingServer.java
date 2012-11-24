package ds02.server;

import ds02.server.service.BillingService;
import ds02.server.service.impl.BillingServiceImpl;
import ds02.server.util.RegistryUtils;
import ds02.server.util.RuntimeUtils;

public class BillingServer {

	public static void main(String[] args) throws Exception {
		try {
			RegistryUtils.bindService(BillingService.class,
					BillingServiceImpl.class);
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		RuntimeUtils.waitForExitCommand();
	}
}
