package com.sbertech.concurrency.hw6;

import java.util.function.Consumer;

public class CountThroughputConsumer implements Consumer<Token> {
	private final long[] throughputs;
	private int index = 0;
	private final int arraySize;

	private int counter = 0;
	private long startTime = 0;
	private final long timePeriod;

	public CountThroughputConsumer(long[] throughputs, int arraySize, long timePeriod) {
		this.throughputs = throughputs;
		this.arraySize = arraySize;
		this.timePeriod = timePeriod * 1_000_000; // in nanoseconds
	}

	@Override
	public void accept(Token token) {
		++counter;
		long currentTime = System.nanoTime();
		if (currentTime - startTime >= timePeriod) {
			throughputs[index % arraySize] = counter;
			++index;
			counter = 0;
			startTime = currentTime;
		}
	}
}
