package com.sbertech.concurrency.hw6;

import java.util.function.Consumer;

public class CountLatencyConsumer implements Consumer<Token> {
	private final long[] latencies;
	private int index = 0;
	private final int arraySize;

	public CountLatencyConsumer(long[] latencies, int arraySize) {
		this.latencies = latencies;
		this.arraySize = arraySize;
	}

	@Override
	public void accept(Token token) {
		long currentTime = System.nanoTime();
		latencies[index % arraySize] = currentTime - token.getTime();
		token.setTime(currentTime);
		++index;
	}
}
