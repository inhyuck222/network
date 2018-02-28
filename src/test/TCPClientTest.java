package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClientTest {
	private static final String SERVER_IP = "192.168.1.19";
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) {
		Socket socket = null;

		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			String data = "hell";
			os.write(data.getBytes("UTF-8"));
			
			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer);
			
			if(readByteCount == -1) {
				return;
			}						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (socket.isClosed() == false && socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
