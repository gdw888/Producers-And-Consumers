package com.gdw888.consumerandproducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
	
	public class PC{
	
		private AtomicInteger work = new AtomicInteger(0);
		private Integer work2 = 0;
		private int max = 10;
		private int min = 0;
		
		private ReentrantLock lock = new ReentrantLock();
		private Condition isFullCond = lock.newCondition();
		private Condition isEmptyCond = lock.newCondition();
		
		private Semaphore isFullSema = new Semaphore(10);
		private Semaphore isEmptySema = new Semaphore(0);

		/*
		public void produce() {
			
			lock.lock();
			while (work == max) {
				{
					try {
						isFullCond.await();
						System.out.println();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			work++;
			System.out.println("Producer:" + Thread.currentThread().getName()+"===" +work);

			//synchronized(isEmptyCond) {
				isEmptyCond.signalAll();
			//}
		
			lock.unlock();
		}
		*/

		public void produce() {
			try {
				isFullSema.acquire();
				System.out.println("Producer:" + Thread.currentThread().getName()+"===" +work.incrementAndGet());
				isEmptySema.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		public void consume() {
			try {
				isEmptySema.acquire();
				System.out.println("Consumer:" + Thread.currentThread().getName()+"===" +work.decrementAndGet());
				isFullSema.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		public void consume2() {
			lock.lock();
			while (work == min) {
				//synchronized(isEmptyCond) {
					try {
						//lock.unlock();
						isEmptyCond.await();
						///lock.lock();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//}
			}
			work--;
			System.out.println("Consumer:" + Thread.currentThread().getName()+"===" +work);
			//synchronized(isFullCond) {
				isFullCond.signalAll();
			//}
			//isFullLock.notifyAll();
			
			lock.unlock();
		}
		*/
	}
	
	public void run() {
		PC pc = new PC();
		List<Thread> producers = new ArrayList<>();
		List<Thread> consumers = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			consumers.add(new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						pc.consume();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}));

			producers.add(new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						pc.produce();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}));
		}
		
		
		producers.stream().forEach(producer -> producer.start());
		consumers.stream().forEach(consumer -> consumer.start());
		//producers.get(0).start();
		//consumers.get(0).start();
		
		try {
			producers.get(0).join();
			consumers.get(0).join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void main(String args[]) {
		Main main = new Main();
		main.run();

		
		
		/*

		for (int i = 0; i < 10; i++) {
			consumers.add(new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						pc.produce();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}));

			producers.add(new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						pc.consume();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}));
		}
		*/
		//producers.stream().forEach(producer -> producer.start());
		//consumers.stream().forEach(consumer -> consumer.start());
		
		//producers.get(0).start();
		//consumers.get(0).start();

	}

}
