package sequencer;

import java.io.IOException;

import UDP.UDPClient;
import UDP.UDPMulticastServer;

public class Tester {
	public static void main(String args[]) throws InterruptedException, IOException {
		String request = "req:create:eftakhairul:islam:rain@gmail.com:12342499:rain:pass123456:van";
		UDPClient uc   =  new UDPClient("localhost", 2000);		
		System.out.println("Testing message for broadcasting: " + request);
		uc.sendOnly(request);		
		
		UDPMulticastServer umc   = new UDPMulticastServer("225.4.5.6", 5000);	  
		String broadcastMessage  = umc.recieve();
		 
		 System.out.println("Broadcast Message: " + broadcastMessage);
		 String[] requestParts = broadcastMessage.split(":");
		 int countet = Integer.parseInt(requestParts[requestParts.length - 1]);
		 uc   =  new UDPClient("localhost", 10000);	
		 System.out.println(uc.send("nak"+":"+countet));
		
	}
}
