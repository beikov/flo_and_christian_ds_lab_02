package ds02.server.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BillingService extends Remote, Serializable {

	public BillingServiceSecure login(String username, String password)
			throws RemoteException;

}
