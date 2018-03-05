package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {
	private static final String SERVER_IP = "192.168.1.19";
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) {
		Socket socket = null;

		try {
			// 1. 소켓 생성
			socket = new Socket();

			// 1-1. Socket buffer size check
			int receiveBufferSize = socket.getReceiveBufferSize();
			int sendBufferSize = socket.getReceiveBufferSize();
			System.out.println("receiveBufferSize : " + receiveBufferSize);
			System.out.println("sendBufferSize : " + sendBufferSize);

			// 1-2. Socket buffer size change
			socket.setReceiveBufferSize(1024 * 10);
			socket.setSendBufferSize(1024 * 10);

			receiveBufferSize = socket.getReceiveBufferSize();
			sendBufferSize = socket.getReceiveBufferSize();
			System.out.println("receiveBufferSize : " + receiveBufferSize);
			System.out.println("sendBufferSize : " + sendBufferSize);

			// 1-3. SO_TIMEOUT
			// socket.setSoTimeout(1);

			// 1-4. SO_NODELAY ( Nagle Algorithm Off )
			socket.setTcpNoDelay(true);

			// 2. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			// 3. I/O Stream 받아 오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			// 4. 쓰기/읽기
			String data = "hello";
			os.write(data.getBytes("UTF-8"));

			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer);

			if (readByteCount == -1) {
				System.out.println("[clinet] disconnected by server");
				return;
			}

			data = new String(buffer, 0, readByteCount, "UTF-8");
			System.out.println("[clinet] received : " + data);
		} catch (ConnectException e) {
			System.out.println("[client] Not Connected");
			// e.printStackTrace();
		} catch (SocketTimeoutException e) {
			System.out.println("[client] Read TimeOut");
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
