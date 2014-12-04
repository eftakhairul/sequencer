package sequencer;

import java.io.IOException;
import java.net.SocketException;

import UDP.UDPMulticastClient;

public class Sender  implements Runnable {

	@Override
	public void run() {
		try {
			String message 		  = null;
			
			
			while(true) {	
				UDPMulticastClient ms = new UDPMulticastClient("225.4.5.6", 5000);				
				synchronized (Sequencer.queue) {
					message = Sequencer.queue.peek();
		        }
				
				if(message != null){	
					System.out.println("Broadcasting message: "+message);
					ms.broadcast(message);	
					Sequencer.queue.poll();
				}	
				
				Thread.sleep(10);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
