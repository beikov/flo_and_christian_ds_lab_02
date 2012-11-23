package ds02.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ds02.server.service.AnalyticsService;
import ds02.server.service.impl.AnalyticsServiceImpl;

public class AnalyticsServer {

	public static void main(String[] args) {
		//System.out.println(new File("").getAbsolutePath());
		
		
		Registry registry = null;
		try {
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}

			AnalyticsService service = new AnalyticsServiceImpl();
			AnalyticsService stub = (AnalyticsService) UnicastRemoteObject.exportObject(service, 0);
			registry = LocateRegistry.createRegistry(1099);
			registry.bind(AnalyticsService.class.getName(), stub);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String input = null;
		do {
			try {
				input = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (!"!exit".equals(input));
		
		try {
			registry.unbind(AnalyticsService.class.getName());
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}