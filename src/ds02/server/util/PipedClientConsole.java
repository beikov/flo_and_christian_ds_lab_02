package ds02.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class PipedClientConsole extends ClientConsole {
	
	private final PipedOutputStream os;
	private final InputStream is;
	
	public PipedClientConsole() {
		this.os = new PipedOutputStream();
		
		try{
			this.is = new PipedInputStream(os);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	public void write(String s){
		try {
			final byte[] bytes = s.getBytes();
			final int size = bytes.length;
			os.write(size);
			os.write(bytes);
			os.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String read() {
		try {
			final int size = is.read();
			
			if(size == -1){
				return null;
			}
			
			final byte[] bytes = new byte[size];
			
			if(is.read(bytes, 0, size) != size){
				throw new IllegalStateException("Protocol error, not enough bytes are available to read");
			}
			
			return new String(bytes);
		} catch (IOException ex) {
			// Don't care about the errors since logging is not required
			// if(!tcpSocket.isClosed()){
			// ex.printStackTrace(System.err);
			// }

			return null;
		}
	}
}
