package sequencer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	
	/*
	 * logs the activities of servers
	 * 
	 * @return: void
	 * 
	 * @throws: SecurityException
	 */
	public static void logFile(String fileName, String Operation) throws SecurityException {
		fileName = fileName + "_log.txt";
		File log = new File(fileName);
		try {
			if (!log.exists()) {
			}
			log.setWritable(true);
			FileWriter fileWriter = new FileWriter(log, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(Operation);
			bufferedWriter.newLine();
			bufferedWriter.close();
			
		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
		}
	}
	
	
	
	@Override
	public void run() { 
		try {			
		
			String data 	= null; 			
			UDPServer us    = new UDPServer("localhost", this.sqnPort);
			while(true) {
				data 				  = us.recieveRequest();
				logFile("nak_server", "nak request details: "+ data);
				System.out.println("nak request details: "+ data);
				String[] requestParts = data.split(":");
				
			
				if(db.get(requestParts[1]) != null){
					System.out.println("response to nak request: "+ db.get(requestParts[1]));
					logFile("nak_server", "response to nak request: "+ db.get(requestParts[1]));
					us.sendResponse(db.get(requestParts[1]));
				}
				
				System.out.println("-----------Sequencer nak server:"+this.sqnPort+ " responsed---------------");
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} 	
	}
	
	public static void main(String [] args) {
		Thread receiverThread = new Thread(new Receiver());
		receiverThread.start();
		System.out.println("Sequencer Receive server is running At port: 2000");
		
		Thread senderThread = new Thread(new Sender());
		senderThread.start();
		System.out.println("Sequencer broadcast server is running at port: 5000");
		
		Thread sequencerThread = new Thread(new Sequencer());
		sequencerThread.start();
		System.out.println("Sequencer message resend server is running at 10000");
	}

	
}
