package ds02.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ds02.server.service.AnalyticsService;
import ds02.server.service.BillingService;
import ds02.server.service.impl.AnalyticsServiceImpl;
import ds02.server.service.impl.BillingServiceImpl;
import ds02.server.util.RuntimeUtils;
import ds02.server.util.RegistryUtils;

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
