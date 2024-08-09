package telran.multithreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class SenderRecieverAppl {

	private static final int N_MESSAGES = 100;
	private static final int N_RECIEVERS = 5;
	private static final int N_MESSAGE_BOXES = 2;

	public static void main(String[] args) throws InterruptedException {
		
		List<BlockingQueue<String>> messageBoxes = createMessageBoxes(N_MESSAGE_BOXES);
		ProducerSender sender = startSender(messageBoxes, N_MESSAGES);
		ConsumerReciever[] recievers = startRecievers(messageBoxes, N_RECIEVERS);
		sender.join();
		stopRecievers(recievers);
		displayResult();
	}

	private static ArrayList<BlockingQueue<String>> createMessageBoxes(int messageBoxNumber) {
		return IntStream.rangeClosed(1, messageBoxNumber)
				.mapToObj(i -> new LinkedBlockingQueue<String>())
				.collect(Collectors.toCollection(ArrayList::new));
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

	private static ConsumerReciever[] startRecievers(List<BlockingQueue<String>> messageBoxes, int nRecievers) {
		ConsumerReciever[] recievers = 
		IntStream.range(0, N_RECIEVERS).mapToObj(i -> {
			ConsumerReciever reciever = new ConsumerReciever();
			reciever.setMessageBox(messageBoxes.get((int) (reciever.getId() % 2)));
			return reciever;
		}).toArray(ConsumerReciever[]::new);
		Arrays.stream(recievers).forEach(ConsumerReciever::start);
		return recievers;
	}

	private static ProducerSender startSender(List<BlockingQueue<String>> messageBoxes, int nMessages) {
		ProducerSender sender = new ProducerSender(messageBoxes, nMessages);
		sender.start();
		return sender;
	}

}
