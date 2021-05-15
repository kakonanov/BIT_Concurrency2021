package com.sbertech.concurrency.hw6;

import com.sbertech.concurrency.hw6.NodeConnector.BlockingQueueNodeConnector;
import com.sbertech.concurrency.hw6.NodeConnector.NodeConnector;
import com.sbertech.concurrency.hw6.node.Node;
import com.sbertech.concurrency.hw6.node.NodeImpl;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TokenRing {
	private final List<NodeConnector> nodeConnectors;
	private final List<Node> nodes;
	private final List<Thread> threads;
	private final int size;

	public TokenRing(int size, List<NodeConnector> nodeConnectors, List<Node> nodes, List<Thread> threads) {
		this.nodeConnectors = nodeConnectors;
		this.nodes = nodes;
		this.threads = threads;
		this.size = size;
	}

	public static TokenRing create(int size, int queueSize) {
		Consumer<Token> consumerCreationTime = (token) -> System.out.println(token);
		Consumer<Token> consumerHandlingTime = (token) -> System.out.println(token);

		// Для 1ой node, nodeConnector - это 1ый nodeConnector из листа
		List<NodeConnector> nodeConnectors = IntStream
				.range(0, size)
				.mapToObj(i -> new BlockingQueueNodeConnector(queueSize))
				.collect(Collectors.toList());

		List<Node> nodes = nodeConnectors
				.stream()
				.map(nodeConnector -> new NodeImpl(nodeConnector, consumerHandlingTime))
				.collect(Collectors.toList());

		List<Thread> threads = IntStream
				.range(0, size)
				.mapToObj(i -> new Thread(
						new TokenRingRunnable(nodes.get(i), nodeConnectors.get((i - 1 + size) % size)),
						String.valueOf(i)
				))
				.collect(Collectors.toList());

		return new TokenRing(size, nodeConnectors, nodes, threads);
	}

	public List<NodeConnector> getNodeConnectors() {
		return nodeConnectors;
	}

	void start() {
		threads.forEach(Thread::start);
	}

	void stop() {
		threads
				.stream()
				.peek(Thread::interrupt)
				.forEach(thread -> {
					try {
						thread.join();
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				});
	}
}
