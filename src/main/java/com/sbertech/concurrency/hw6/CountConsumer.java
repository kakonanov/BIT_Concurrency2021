package com.sbertech.concurrency.hw6;

import java.util.function.Consumer;

public class CountConsumer implements Consumer<Token> {
	private final CountThroughputConsumer countThroughputConsumer;
	private final CountLatencyConsumer countLatencyConsumer;
	private final Consumer<Token> consumer;

	public CountConsumer(CountThroughputConsumer countThroughputConsumer, CountLatencyConsumer countLatencyConsumer, Consumer<Token> consumer) {
		this.countThroughputConsumer = countThroughputConsumer;
		this.countLatencyConsumer = countLatencyConsumer;
		this.consumer = consumer;
	}

	@Override
	public void accept(Token token) {
		countLatencyConsumer.accept(token);
		countThroughputConsumer.accept(token);
		consumer.accept(token);
	}
}
