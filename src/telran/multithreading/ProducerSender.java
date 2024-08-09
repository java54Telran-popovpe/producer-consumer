package telran.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ProducerSender extends Thread {
	private BlockingQueue<String> messageBox;
	private int nMessages;
	
	public ProducerSender(BlockingQueue<String> messageBox, int nMessages) {
		this.messageBox = messageBox;
		this.nMessages = nMessages;
	}
	
	
	@Override
	public void run() {
		IntStream.rangeClosed(1, nMessages)
			.mapToObj( i -> "message" + i )
			.forEach( m -> {
				try {
					messageBox.put(m);
				} catch (InterruptedException e) {
					
				}
			});
	}
	
	

}
