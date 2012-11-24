package ds02.server;

import ds02.server.service.impl.BillingServiceImpl;
import ds02.server.util.RegistryUtils;
import ds02.server.util.RuntimeUtils;

public class BillingServer {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			usage();
		}

		try {
			RegistryUtils.bindService(args[0], BillingServiceImpl.class);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		RuntimeUtils.waitForExitCommand();
	}

	private static void usage() {
		System.out.println("Usage: " + BillingServer.class.getSimpleName()
				+ " <billingServerBinding>");
		System.exit(1);
	}
}
