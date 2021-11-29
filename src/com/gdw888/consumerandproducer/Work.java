package com.gdw888.consumerandproducer;

import java.util.Stack;

public class Work {

	private int work;
	private Stack<Object> works;
	
	private final int min = 0;
	private final int max = 10;

	public Work() {}
	
	public void produceWork() {
		work++;
	}
	
	public int consumeWork() {
		return work--;
	}
}
