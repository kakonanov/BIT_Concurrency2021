package com.sbertech.concurrency.hw6.node;

import com.sbertech.concurrency.hw6.NodeConnector.NodeConnector;
import com.sbertech.concurrency.hw6.Token;

import java.util.function.Consumer;

public class NodeImpl implements Node {
	private final NodeConnector nodeConnector;
	private final Consumer<Token> consumer;

	public NodeImpl(NodeConnector nodeConnector, Consumer<Token> consumer) {
		this.nodeConnector = nodeConnector;
		this.consumer = consumer;
	}

	@Override
	public void handleToken(Token token) throws InterruptedException {
		consumer.accept(token);
		nodeConnector.put(token);
	}
}
