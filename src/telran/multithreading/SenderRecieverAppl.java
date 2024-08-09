package telran.multithreading;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;


public class SenderRecieverAppl {

	private static final int N_MESSAGES = 10000;
	private static final int N_RECIEVERS = 10;

	public static void main(String[] args) throws InterruptedException {
		
		BlockingQueue<String> messageBox = new LinkedBlockingQueue<String>();
		ProducerSender sender = startSender(messageBox, N_MESSAGES);
		ConsumerReciever[] recievers = startRecievers(messageBox, N_RECIEVERS);
		sender.join();
		stopRecievers(recievers);
		displayResult();
		
		

	}

	private static void displayResult() {
		System.out.printf("counter of processed messages is %d\n", ConsumerReciever.getMessagesCounter());
	}

	private static void stopRecievers(ConsumerReciever[] recievers) throws InterruptedException {
		for(ConsumerReciever reciever: recievers) {
			reciever.interrupt();
			reciever.join();
		}
		
	}

	private static ConsumerReciever[] startRecievers(BlockingQueue<String> messageBox, int nRecievers) {
		ConsumerReciever[] recievers = 
		IntStream.range(0, N_RECIEVERS).mapToObj(i -> {
			ConsumerReciever reciever = new ConsumerReciever();
			reciever.setMessageBox(messageBox);
			return reciever;
		}).toArray(ConsumerReciever[]::new);
		Arrays.stream(recievers).forEach(ConsumerReciever::start);
		return recievers;
	}

	private static ProducerSender startSender(BlockingQueue<String> messageBox, int nMessages) {
		ProducerSender sender = new ProducerSender(messageBox, nMessages);
		sender.start();
		return sender;
	}

}
