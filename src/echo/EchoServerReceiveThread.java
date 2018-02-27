package echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceiveThread extends Thread {

	private Socket socket;

	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// 4. 연결 성공
		InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();

		int remoteHostPort = remoteSocketAddress.getPort();
		String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();
		consoleLog("connected from " + remoteHostAddress + ":" + remoteHostPort);

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
					consoleLog("disconnected by client");
					break;
				}

				String data = new String(buffer, 0, readByteCount, "UTF-8");
				consoleLog("received : " + data);

				// 7. 데이터 쓰기(write)
				os.write(data.getBytes("UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket.isClosed() == false && is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			if (socket.isClosed() == false && os != null) {

				os.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void consoleLog(String log) {
		System.out.println("[server:" + getId() + "] " + log);
	}

}
