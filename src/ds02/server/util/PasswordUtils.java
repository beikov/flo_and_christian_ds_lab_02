package ds02.server.util;

import java.security.MessageDigest;

public final class PasswordUtils {

	private static final String HEXES = "0123456789ABCDEF";

	private PasswordUtils() {

	}

	private static String bytesToHex(byte[] bytes) {
		if (bytes == null) {
			return null;
		}

		final StringBuilder hex = new StringBuilder(2 * bytes.length);

		for (int i = 0; i < bytes.length; i++) {
			hex.append(HEXES.charAt((bytes[i] & 0xF0) >> 4)).append(
					HEXES.charAt((bytes[i] & 0x0F)));
		}

		return hex.toString();
	}

	private static String getHashHexString(String text, String algo,
			String salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algo);
			byte[] bytes = null;

			digest.reset();

			if (salt != null) {
				digest.update(salt.getBytes("UTF-8"));
			}

			digest.update(text.getBytes("UTF-8"));
			bytes = digest.digest();

			for (int i = 0; i < iterations; i++) {
				digest.reset();
				bytes = digest.digest(bytes);
			}

			return bytesToHex(bytes);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String hashPassword(String password) {
		return getHashHexString(password, "MD5", null, 0);
	}

	public static boolean matches(String plain, String hash) {
		return hash != null && hash.equalsIgnoreCase(hashPassword(plain));
	}
}
