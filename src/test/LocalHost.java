package test;

import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		InetAddress localHost = null;
		ByteArrayInputStream byteInput = null;
		try {
			localHost = InetAddress.getLocalHost();
			
			String hostname = localHost.getHostName();
			String hostaddress = localHost.getHostAddress();
			byte[] addresses = localHost.getAddress();
			
			System.out.println(hostname);
			System.out.println(hostaddress);
			byteInput = new ByteArrayInputStream(addresses);
			int data;
			for(int i = 0; i < addresses.length; i++) {
				data = byteInput.read();
				System.out.print(data);
				//System.out.print(addresses[i] & 0x000000ff);
				if(i < 3) {
					System.out.print(".");	
				}
				
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

}
