package ds02.server.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtils {

	private static final Logger LOG = Logger.getLogger(PropertiesUtils.class);

	public static Properties getProperties(String fileName) {
		InputStream is = ClassLoader.getSystemResourceAsStream(fileName);

		if (is == null) {
			LOG.error("Properties file not found!");
			return null;
		}

		try {
			Properties p = new Properties();
			p.load(is);
			return p;
		} catch (Exception e) {
			LOG.error("Could not load Properties!", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception s) {
					// IGNORE
				}
			}
		}

		return null;
	}
}
