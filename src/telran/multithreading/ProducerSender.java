package telran.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ProducerSender extends Thread {
	private List<BlockingQueue<String>> messageBoxes;
	private int nMessages;
	

	public ProducerSender(List<BlockingQueue<String>> messageBoxes, int nMessages) {
		this.messageBoxes = messageBoxes;
		this.nMessages = nMessages;
	}

	@Override
	public void run() {
		IntStream.rangeClosed(1, nMessages)
			.forEach( i -> {
				try {
					messageBoxes.get( i % 2 ).put("message" + i);
				} catch (InterruptedException e) {
					
				}
			});
	}
	
	

}
