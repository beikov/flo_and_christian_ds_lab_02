package ds02.server.util;

import java.rmi.RemoteException;

public interface Pingable {
	public void ping() throws RemoteException;
}
