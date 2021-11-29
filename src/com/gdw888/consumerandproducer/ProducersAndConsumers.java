package com.gdw888.consumerandproducer;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ProducersAndConsumers {
	
	final int numConsumers = 4;
	final int numProducers = 5;
	final int numElements  = 10;
	
	private final Semaphore fillCount  = new Semaphore(0, true);
	private final Semaphore emptyCount  = new Semaphore(numElements, true);
	private final Semaphore mutex = new Semaphore(1, true);

	ArrayList<String> elements;
	
	Producer producerArray[];
	Consumer consumerArray[];
	
	public ProducersAndConsumers() {
		elements = new ArrayList<String>();
		producerArray = new Producer[numProducers];
		consumerArray = new Consumer[numConsumers];
		
		for (int i = 0; i < numProducers; i ++) {
			producerArray[i] = new Producer("PRODUCER"+i);
			producerArray[i].start();
		}
		
		for (int i = 0; i < numConsumers; i ++) {
			consumerArray[i] = new Consumer("CONSUMER"+i);
			consumerArray[i].start();
		}
		
	}
	
	public void addElement(String threadname, String value) throws InterruptedException {
		mutex.acquire();
		elements.add(value);
		System.out.println(threadname+": "+ elements.size());
		mutex.release();
	}
	
	public String removeElement(String threadname) throws InterruptedException {
		String result;
		
		mutex.acquire();
		result= elements.remove(0);
		System.out.println(threadname+": "+ elements.size());
		mutex.release();
		
		return result;
	}


	public class Producer extends Thread{

		String threadName;
		
		public Producer(String threadName) {
			this.threadName = threadName;
		}

		@Override
		public void run() {
			while (true){
				try {
					emptyCount.acquire();
					addElement(threadName, new java.util.Date().toString());
					
					fillCount.release();
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public class Consumer extends Thread{

		String threadName;
		
		public Consumer(String threadName) {
			this.threadName = threadName;
		}
		
		@Override
		public void run() {
			while (true){
			
				try {
					fillCount.acquire();
					String removedElement = removeElement(threadName);
					emptyCount.release();
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

	public static void main (String[] args){
		ProducersAndConsumers pc = new ProducersAndConsumers();
	}
}
