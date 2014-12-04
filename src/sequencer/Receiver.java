package sequencer;

import java.net.SocketException;

import UDP.UDPServer;

public class Receiver implements Runnable {

	@Override
	public void run() {		
		try {
			
			UDPServer us = null;
			while(true) {
				us = new UDPServer("localhost", 2000);
				String request = us.recieveRequest();		
				String sqNum   = Integer.toString(Sequencer.seqeuenNum++);
				request 	  += ":" + sqNum;
				System.out.println("Receive message: "+ request);
				
				synchronized (Sequencer.queue) {
					Sequencer.queue.add(request);		
		        }			
				
				if(Sequencer.db.get(sqNum) == null) {
					Sequencer.db.put(sqNum, request);
				}
				
				us.close();
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
