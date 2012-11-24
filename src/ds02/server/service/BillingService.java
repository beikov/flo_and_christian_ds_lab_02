package ds02.server.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import ds02.server.util.Pingable;

public interface BillingService extends Remote, Serializable, Pingable {

	public BillingServiceSecure login(String username, String password)
			throws RemoteException;

}
