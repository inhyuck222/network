package chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClientApp {
	private static final String SERVER_IP = "192.168.1.19";
	private static final int SERVER_PORT = 6060;

	public static void main(String[] args) {
		String name = null;
		Scanner scanner = new Scanner(System.in);
		
		Socket socket = new Socket();

		while( true ) {
			
			System.out.println("대화명을 입력하세요.");
			System.out.print(">>> ");
			name = scanner.nextLine();
			
			if (name.isEmpty() == false ) {
				break;
			}
			
			System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
		}
		
		scanner.close();
		PrintWriter pw = null;
		BufferedReader br = null;
		try {
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String joinStr = "join:" + name;
		
		if(br == null) {
			return;
		}		
		if(pw == null) {
			return;
		}
		
		pw.println(joinStr);
		
		new ChatWindow(name, socket, pw, br).show();
	}

}
