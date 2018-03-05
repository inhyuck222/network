package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();

			// Time-wait 상태에서 서버 재실행이 가능하게 끔 함
			serverSocket.setReuseAddress(true);

			// 2. 바인딩(Binding)
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));

			System.out.println("[server] binding " + localhostAddress + ":" + SERVER_PORT);

			// 3. 연결 요청 대기(Accept)
			socket = serverSocket.accept(); // blocking

			// 4. 연결 성공(단순 출력만)
			InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			int remoteHostPort = remoteSocketAddress.getPort();
			String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();
			System.out.println("[server] connected from " + remoteHostAddress + ":" + remoteHostPort);

			// 5. I/O Stream 받아오기
			InputStream is = null;
			OutputStream os = null;

			try {
				is = socket.getInputStream();
				os = socket.getOutputStream();

				while (true) {
					// 6. 데이터 읽기(read)
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); // blocking

					if (readByteCount == -1) {
						System.out.println("[server] disconnected by client");
						break;
					}

					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[server] received : " + data);

					// 7. 데이터 쓰기(write)
					os.write(data.getBytes("UTF-8"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket.isClosed() == false && is != null) {
					is.close();
				}
				if (socket.isClosed() == false && os != null) {
					os.close();
				}
			}
		} catch (SocketException e) {
			// 클라이언트가 정상적으로 종료되지 않았는데 서버에서 read를 하면 SocketException 발생
			System.out.println("[server] sudden closed by client");
			e.printStackTrace();
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
