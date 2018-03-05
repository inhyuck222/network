package echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPEchoClient {
	private static final String SERVER_IP = "192.168.1.19";
	private static final int SERVER_PORT = 5000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket socket = null;
		Scanner scanner = null;

		try {
			// 0. 키보드 연결
			scanner = new Scanner(System.in);

			// 1. 소켓 생성
			socket = new DatagramSocket();

			while (true) {
				// 2. 사용자 입력
				System.out.print(">>");
				String message = scanner.nextLine();

				if ("".equals(message)) {
					continue;
				}

				if ("quit".equals(message)) {
					break;
				}

				// 3. 데이터 송신 처리
				byte[] sendData = message.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						new InetSocketAddress(SERVER_IP, SERVER_PORT));

				// 4. 데이터 전송 처리
				socket.send(sendPacket);
				
				// 5. 데이터 수신 처리
				DatagramPacket receivePacket = new DatagramPacket (new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket);
				
				message = new String(receivePacket.getData(), 0 , receivePacket.getLength(), "UTF-8");
				System.out.println("<<" + message);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			
			if(socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}

}
