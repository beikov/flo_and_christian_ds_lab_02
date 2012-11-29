package ds02.server.util;

import java.io.PrintStream;

public abstract class ClientConsole {

	public static final ClientConsole out = new PrintStreamClientConsole(System.out);
	
	public static PipedClientConsole piped(){
		return new PipedClientConsole();
	}
	
	public abstract void write(String s);
	
	private static class PrintStreamClientConsole extends ClientConsole {
		
		private final PrintStream ps;
		
		public PrintStreamClientConsole(PrintStream ps) {
			this.ps = ps;
		}

		@Override
		public void write(String s) {
			ps.println(s);
			ps.flush();
		}
	}
}
