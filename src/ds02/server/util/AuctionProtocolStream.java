package ds02.server.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AuctionProtocolStream {

	private final OutputStream out;
	private final InputStream in;
	
	public AuctionProtocolStream(OutputStream out, InputStream in) {
		this.out = new BufferedOutputStream(out);
		this.in = new BufferedInputStream(in);
	}

	public void write(String response) {
		try {
			final byte[] bytes = response.getBytes();
			final int size = bytes.length;
			out.write(size);
			out.write(bytes);
			out.flush();
		} catch (IOException ex) {
			// Don't care about the errors since logging is not required
			// if(!tcpSocket.isClosed()){
			// ex.printStackTrace(System.err);
			// }
		}
	}

	public String read() {
		try {
			final int size = in.read();
			
			if(size == -1){
				return null;
			}
			
			final byte[] bytes = new byte[size];
			
			if(in.read(bytes, 0, size) != size){
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
