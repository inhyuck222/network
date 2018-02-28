package chat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ChatWindow {

	private Frame frame;
	private Panel pannel;
	private Button buttonSend;
	private TextField textField;
	private TextArea textArea;

	private Socket socket;
	private PrintWriter pw;
	private BufferedReader br;
	private boolean exit;

	public ChatWindow(String name, Socket socket, PrintWriter pw, BufferedReader br) {
		frame = new Frame(name);
		pannel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);

		this.socket = socket;
		this.pw = pw;
		this.br = br;
		this.exit = false;

		Thread thread = new ChatClientReceiveThread();
		thread.start();
	}

	public void show() {
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				sendMessage();
			}
		});

		// Textfield
		textField.setColumns(80);
		textField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if (keyCode == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});

		// Pannel
		pannel.setBackground(Color.LIGHT_GRAY);
		pannel.add(textField);
		pannel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, pannel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.pack();
	}

	private void sendMessage() {
		String message = textField.getText();
		if (message == null) {
			return;
		}
		if ("quit".equals(message)) {
			exit = true;
			textField.setText("");
			textField.requestFocus();
			pw.println("quit:");
			return;
		}
		pw.println("message:" + message);

		textField.setText("");
		textField.requestFocus();
	}

	private class ChatClientReceiveThread extends Thread {

		@Override
		public void run() {
			try {
				while (true) {
					String message;

					message = br.readLine();
					if (exit) {
						textArea.append("퇴장하셨습니다.\n");
						break;
					}
					if (message != null) {
						textArea.append(message + "\n");
					}
				}
			} catch (SocketException e) {
				System.out.println("서버 비정상 종료");
				e.printStackTrace();				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (pw != null) {
					pw.close();
				}
								
				System.exit(0);
			}
		}

	}

}