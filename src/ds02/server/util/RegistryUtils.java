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
	private static final ConcurrentMap<String, Object> REMOTE_SET = new ConcurrentHashMap<String, Object>();
	private static final Object STUB = new Object();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				final Iterator<String> it = REMOTE_SET.keySet().iterator();

				while (it.hasNext()) {
					try {
						getRegistry().unbind(it.next());
					} catch (Exception e) {
						// IGNORE
					}
					it.remove();
				}
			}
		});
	}

	public static Registry getRegistry() {
		try {
			Properties p = PropertiesUtils.getProperties("registry.properties");
			final String host = p.getProperty("registry.host", "localhost");
			final int port = Integer.parseInt(p.getProperty("registry.port",
					"1099"));

			return LocateRegistry.getRegistry(host, port);
		} catch (Exception e) {
			LOG.error("Could not locate Registry!", e);
		}

		return null;
	}

	public static <T extends Remote> T getRemote(Class<T> clazz) {
		try {
			return clazz.cast(getRegistry().lookup(clazz.getName()));
		} catch (Exception e) {
			LOG.error(
					"Could not lookup Remote Object of type " + clazz.getName(),
					e);
		}
		return null;
	}

	public static <T extends Remote, U extends T> void bindService(
			Class<T> clazz, U remote) {
		try {
			REMOTE_SET.put(clazz.getName(), STUB);
			getRegistry().bind(clazz.getName(),
					UnicastRemoteObject.exportObject(remote, 0));
		} catch (Exception e) {
			throw new RuntimeException("Could not bind!", e);
		}

	}

	@SuppressWarnings("unchecked")
	public static <T extends Remote> T exportObject(T remote) {
		try {
			return (T) UnicastRemoteObject.exportObject(remote, 0);
		} catch (Exception e) {
			throw new RuntimeException("Could not export object!", e);
		}
	}

	private RegistryUtils() {

	}
}
