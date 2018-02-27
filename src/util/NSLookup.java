package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("> ");
			
			InetAddress[] addresses = null;
			String domainAddress = scanner.nextLine();
			
			if(domainAddress.equals("exit")) {
				break;
			}
			
			try {
				addresses = InetAddress.getAllByName(domainAddress);
				if (addresses != null) {
					for (InetAddress address : addresses) {
						System.out.println(address.getHostAddress());
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		
		if(scanner != null) {
			scanner.close();
		}
	}

}
