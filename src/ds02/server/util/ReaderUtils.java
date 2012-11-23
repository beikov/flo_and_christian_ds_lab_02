package ds02.server.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class ReaderUtils {
	
	private ReaderUtils() {
		
	}
	
	public static void waitForExitCommand() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String input = null;
		
		do {
			try {
				input = reader.readLine();
			} catch (Exception e) {
				// ignore
			}
		} while (!"!exit".equals(input) && input != null);
	}
}
