package sequencer;

import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import UDP.UDPServer;



public class Sequencer  implements Runnable {
	
	public static Queue<String> queue 	 = new LinkedList<String>();
	public static Map<String, String> db = Collections.synchronizedMap(new HashMap<String, String>(100000));
	public static int 	   seqeuenNum    = 1;
	private int sqnPort                  = 10000;
	
	@Override
	public void run() { 
		try {			
		
			String data 	= null; 			
			UDPServer us    = new UDPServer("localhost", this.sqnPort);
			while(true) {		
				
				System.out.println("-----------Sequencer Resend Mgs Resquest Server:"+ this.sqnPort+"---------------");
				data 				  = us.recieveRequest();
				System.out.println("Incoming request details: "+ data);
				String[] requestParts = data.split(":");
				
			
				if(db.get(requestParts[1]) != null){
					System.out.println("Response to nak details: "+ db.get(requestParts[1]));
					us.sendResponse(db.get(requestParts[1]));
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} 	
	}
	
	public static void main(String [] args) {
		Thread receiverThread = new Thread(new Receiver());
		receiverThread.start();
		System.out.println("Sequencer Receive server is running.");
		
		Thread senderThread = new Thread(new Sender());
		senderThread.start();
		System.out.println("Sequencer broadcast server is running.");
		
		Thread sequencerThread = new Thread(new Sequencer());
		sequencerThread.start();
		System.out.println("Sequencer message resend server is running.");
	}

	
}
