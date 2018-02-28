package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class ChatServerProcessThread extends Thread {
	private Socket socket;
	List<PrintWriter> printWriterList;
	private BufferedReader br;
	private PrintWriter pw;
	private String myName;

	public ChatServerProcessThread(Socket socket, List<PrintWriter> printWriterList) {
		this.socket = socket;
		this.printWriterList = printWriterList;
	}

	@Override
	public void run() {
		checkRemoteConnection();
		
		setInputStream();
		setOutputStream();

		try {
			while (true) {
				String message = br.readLine();
				if (message == null) {
					System.out.println("클라이언트로부터 연결 해제");
					break;
				}
				
				System.out.println(message);
				
				String[] tokens = message.split(":");

				if ("join".equals(tokens[0])) {
					doJoin(tokens[1]);
				} else if ("message".equals(tokens[0])) {
					sendMeesage(tokens[1]);
				} else if ("quit".equals(tokens[0])) {
					doQuit();
					break;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void doQuit() {		
		synchronized (printWriterList) {
			if (printWriterList == null) {
				return;
			}
			Iterator<PrintWriter> iter = printWriterList.iterator();
			while (iter.hasNext()) {
				PrintWriter printWriter = iter.next();
				printWriter.println(myName + "님이 퇴장 하였습니다.");
			}
			printWriterList.remove(pw);
		}
	}

	private void sendMeesage(String message) {
		synchronized (printWriterList) {
			System.out.println(printWriterList.size());
			if (printWriterList == null) {
				return;
			}
			Iterator<PrintWriter> iter = printWriterList.iterator();
			while (iter.hasNext()) {
				PrintWriter printWriter = iter.next();
				if(printWriter.hashCode() == pw.hashCode()) {
					continue;
				}
				printWriter.println(myName + ":" + message);
			}	
		}		
	}

	private void doJoin(String name) {
		this.myName = name;
		if (pw == null) {
			return;
		}
		
		synchronized (printWriterList) {
			if (printWriterList == null) {
				return;
			}
			Iterator<PrintWriter> iter = printWriterList.iterator();
			while (iter.hasNext()) {
				PrintWriter printWriter = iter.next();
				printWriter.println(myName + "님이 입장 하였습니다.");
			}
			printWriterList.add(pw);	
		}		
	}
	
	private void checkRemoteConnection() {
		InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();

		int remoteHostPort = remoteSocketAddress.getPort();
		String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();
		consoleLog("connected from " + remoteHostAddress + ":" + remoteHostPort);
	}
	
	public void setInputStream() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setOutputStream() {
		try {
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void consoleLog(String log) {
		System.out.println("[server:" + getId() + "] " + log);
	}

}
