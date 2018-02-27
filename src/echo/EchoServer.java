package echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int SERVER_PORT = 6000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();

			// 2. 바인딩(Binding)
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));
			consoleLog("binding " + localhostAddress + ":" + SERVER_PORT);

			while (true) {
				// 3. 연결 요청 대기(Accept)
				socket = serverSocket.accept(); // blocking
				Thread thread = new EchoServerReceiveThread(socket);
				thread.start();
			}
		} catch (SocketException e) {
			// 클라이언트가 정상적으로 종료되지 않았는데 서버에서 read를 하면 SocketException 발생
			consoleLog("sudden closed by client");
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

	private static void consoleLog(String log) {
		System.out.println("[server " + Thread.currentThread().getId() + "]" + log);
	}
}
