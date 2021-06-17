package com.sbertech.concurrency.hw6;

import java.util.function.Consumer;

public class CountLatencyConsumer implements Consumer<Token> {
	private final long[] latencies;
	private int index = 0;
	private final int arraySize;
	private final int nodeId;
	private final int size;

	public CountLatencyConsumer(long[] latencies, int arraySize, int nodeId, int size) {
		this.latencies = latencies;
		this.arraySize = arraySize;
		this.nodeId = nodeId;
		this.size = size;
	}

	@Override
	public void accept(Token token) {
		if (token.getId() % size == nodeId) {
			long currentTime = System.nanoTime();
			latencies[index % arraySize] = currentTime - token.getTime();
			token.setTime(currentTime);
			++index;
		}
	}
}
