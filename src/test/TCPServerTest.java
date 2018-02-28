package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerTest {
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket();

			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));

			socket = serverSocket.accept();

			InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			int remoteHostPort = remoteSocketAddress.getPort();
			String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();
			System.out.println(remoteHostAddress + ":" + remoteHostPort);

			InputStream is = null;
			OutputStream os = null;

			try {
				is = socket.getInputStream();
				os = socket.getOutputStream();

				while (true) {
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer);

					if (readByteCount == -1) {
						break;
					}

					String data = new String(buffer, 0, readByteCount, "UTF-8");
					os.write(data.getBytes("UTF-8"));
					System.out.println(data);
				}

			} catch (IOException e) {
				// TODO: handle exception
			} finally {
				if (socket.isClosed() == false && is != null) {
					is.close();
				}
				if (socket.isClosed() == false && os != null) {
					os.close();
				}
			}

		} catch (IOException e) {
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
