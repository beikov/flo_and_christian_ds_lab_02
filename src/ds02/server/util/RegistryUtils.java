package ds02.server.util;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

public final class RegistryUtils {

	private static final Logger LOG = Logger.getLogger(RegistryUtils.class);
	private static final ConcurrentMap<Remote, Object> LOOKEDUP_REMOTES = new ConcurrentHashMap<Remote, Object>();
	private static final ConcurrentMap<String, Object> BOUND_REMOTES = new ConcurrentHashMap<String, Object>();

	private static final Object STUB = new Object();

	static {
		RuntimeUtils.addShutdownHook(new Runnable() {
			@Override
			public void run() {
				final Iterator<String> it = BOUND_REMOTES.keySet().iterator();

				while (it.hasNext()) {
					try {
						getOrCreateRegistry().unbind(it.next());
					} catch (Exception e) {
						// IGNORE
					}
					it.remove();
				}

				final Iterator<Remote> iter = LOOKEDUP_REMOTES.keySet()
						.iterator();

				while (iter.hasNext()) {
					try {
						UnicastRemoteObject.unexportObject(iter.next(), true);
					} catch (Exception e) {
						// IGNORE
					}
					iter.remove();
				}
			}
		});
	}

	public static Registry getOrCreateRegistry() {
		try {
			Properties p = PropertiesUtils.getProperties("registry.properties");
			final String host = p.getProperty("registry.host", "localhost");
			final int port = Integer.parseInt(p.getProperty("registry.port",
					"1099"));

			Registry r = null;

			try {
				r = LocateRegistry.getRegistry(host, port);
				r.list();
			} catch (Exception e) {
				r = null;
			}

			if (r == null) {
				r = LocateRegistry.createRegistry(port);
			}
			return r;
		} catch (Exception e) {
			LOG.error("Could not locate Registry!", e);
		}

		return null;
	}

	public static <T extends Remote> T getRemote(Class<T> clazz) {
		return getRemote(clazz.getName());
	}

	public static <T extends Remote> T getRemote(String name) {
		try {
			@SuppressWarnings("unchecked")
			final T remote = (T) getOrCreateRegistry().lookup(name);
			LOOKEDUP_REMOTES.put(remote, STUB);
			return remote;
		} catch (Exception e) {
			LOG.error("Could not lookup Remote Object of type " + name, e);
		}
		return null;
	}

	public static <T extends Remote, U extends T> void bindService(
			Class<T> clazz, Class<U> remote) {
		bindService(clazz.getName(), remote);
	}

	public static <U extends Remote> void bindService(String name,
			Class<U> remote) {
		try {
			BOUND_REMOTES.put(name, STUB);
			getOrCreateRegistry().rebind(name,
					exportObject(remote.newInstance()));
		} catch (Exception e) {
			throw new RuntimeException("Could not bind!", e);
		}

	}

	@SuppressWarnings("unchecked")
	public static <T extends Remote> T exportObject(T remote) {
		try {
			LOOKEDUP_REMOTES.put(remote, STUB);
			return (T) UnicastRemoteObject.exportObject(remote, 0);
		} catch (Exception e) {
			throw new RuntimeException("Could not export object!", e);
		}
	}

	private RegistryUtils() {

	}
}
