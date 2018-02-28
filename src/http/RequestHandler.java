package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private static final String DOCUMENT_ROOT = "./webapp";

	private Socket socket;

	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			consoleLog("connected from " + inetSocketAddress.getAddress().getHostAddress() + ":"
					+ inetSocketAddress.getPort());

			// get IOStream
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			OutputStream os = socket.getOutputStream();

			String request = null;
			while (true) {
				String line = br.readLine();
				if (line == null || "".equals(line)) {
					break;
				}

				if (request == null) {
					request = line;
					break;
				}
			}

			consoleLog(request);

			// 요청 분석
			String[] tokens = request.split(" ");
			if (tokens[0].equals("GET")) {
				responseStaticResource(os, tokens[1], tokens[2]);
			} else {
				responseStatic400Error(os, tokens[2]);
			}

		} catch (Exception ex) {
			consoleLog("error:" + ex);
		} finally {
			// clean-up
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException ex) {
				consoleLog("error:" + ex);
			}
		}
	}

	private void responseStaticResource(OutputStream os, String url, String protocol) throws IOException {
		if ("/".equals(url)) {
			url = "index.html";
		}

		File file = new File(DOCUMENT_ROOT + "/" + url);
		if (file.exists() == false) {
			response404Error(os, protocol);
			return;
		}

		byte[] body = Files.readAllBytes(file.toPath());
		String mimeType = Files.probeContentType(file.toPath());

		// header 전송
		os.write((protocol + " 200 OK\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + mimeType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		os.write("\r\n".getBytes());

		// body 전송
		os.write(body);
	}

	private void response404Error(OutputStream os, String protocol) throws IOException {
		File file = new File(DOCUMENT_ROOT + "/error/404.html");

		byte[] body = Files.readAllBytes(file.toPath());
		String mimeType = Files.probeContentType(file.toPath());

		// header 전송
		os.write((protocol + " 404 Not Found\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + mimeType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		os.write("\r\n".getBytes());

		// body 전송
		os.write(body);
	}

	private void responseStatic400Error(OutputStream os, String protocol) throws IOException {
		File file = new File(DOCUMENT_ROOT + "/error/400.html");
		byte[] body = null;
		if (file.exists()) {
			body = Files.readAllBytes(file.toPath());
		}

		// header 전송
		os.write((protocol + " 400 Bad Request\r\n").getBytes("UTF-8"));
		

		// body 전송
		if(body != null) {
			String mimeType = Files.probeContentType(file.toPath());
			os.write(("Content-Type:" + mimeType + "; charset=utf-8\r\n").getBytes("UTF-8"));
			os.write("\r\n".getBytes());
			os.write(body);
		}
		
	}

	private void consoleLog(String message) {
		System.out.println("[RequestHandler#" + getId() + "] " + message);
	}
}