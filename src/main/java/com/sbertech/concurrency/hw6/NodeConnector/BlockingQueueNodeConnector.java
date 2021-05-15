package com.sbertech.concurrency.hw6.NodeConnector;

import com.sbertech.concurrency.hw6.Token;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueNodeConnector implements NodeConnector {
	private final BlockingQueue<Token> blockingQueue;

	public BlockingQueueNodeConnector(int queueSize) {
		this.blockingQueue = new ArrayBlockingQueue<>(queueSize);
	}

	@Override
	public void put(Token token) throws InterruptedException {
		blockingQueue.put(token);
	}

	@Override
	public Token take() throws InterruptedException {
		return blockingQueue.take();
	}
}
