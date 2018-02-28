package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	
	private static final int SERVER_PORT = 6060;
	private ServerSocket serverSocket;
	private List<PrintWriter> printWriterList;
	
	public ChatServer() {
		printWriterList = new ArrayList<PrintWriter>();
	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.createServerSocket();
		
		while(true) {
			boolean alive = server.accept();
			if(alive == false) {
				break;
			}
		}
	}
	
	private void createServerSocket() {
		try {
			serverSocket = new ServerSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(serverSocket == null) {
			return;
		}
		
		binding();
	}
	
	private void binding() {
		String localhostAddress = null;
		try {
			localhostAddress = InetAddress.getLocalHost().getHostAddress();
			if(localhostAddress != null) {
				serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));	
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean accept() {
		if(serverSocket == null) {
			return false;
		}
		
		Socket socket = null;		
		try {
			socket = serverSocket.accept();
			if(socket != null) {
				Thread thread = new ChatServerProcessThread(socket, printWriterList);
				
				if(thread != null) {
					thread.start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
