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
	private final long [] latencies;
	private final long[] throughputs;


	public TokenRing(int size, List<NodeConnector> nodeConnectors, List<Node> nodes, List<Thread> threads, long[] latencies, long[] throughputs) {
		this.nodeConnectors = nodeConnectors;
		this.nodes = nodes;
		this.threads = threads;
		this.size = size;
		this.latencies = latencies;
		this.throughputs = throughputs;
	}

	public static TokenRing create(int size, int queueSize, int latencyArraySize, int throughputArraySize, long periodTime) {
		long[] latencies = new long[latencyArraySize];
		long[] throughputs = new long[throughputArraySize];

		CountLatencyConsumer countLatencyConsumer = new CountLatencyConsumer(latencies, latencyArraySize);
		CountThroughputConsumer countThroughputConsumer = new CountThroughputConsumer(throughputs, throughputArraySize, periodTime);
		Consumer<Token> countConsumer = token -> {
			countLatencyConsumer.accept(token);
			countThroughputConsumer.accept(token);
		};
		Consumer<Token> skipConsumer = token -> token = token;

		// Для 1ой node, nodeConnector - это 1ый nodeConnector из листа
		List<NodeConnector> nodeConnectors = IntStream
				.range(0, size)
				.mapToObj(i -> new BlockingQueueNodeConnector(queueSize))
				.collect(Collectors.toList());

		List<Node> nodes = nodeConnectors
				.stream()
				.map(nodeConnector -> new NodeImpl(nodeConnector, skipConsumer))
				.collect(Collectors.toList());

		nodes.set(0, new NodeImpl(nodeConnectors.get(0), countConsumer));

		List<Thread> threads = IntStream
				.range(0, size)
				.mapToObj(i -> new Thread(
						new TokenRingRunnable(nodes.get(i), nodeConnectors.get((i - 1 + size) % size)),
						String.valueOf(i)
				))
				.collect(Collectors.toList());

		return new TokenRing(size, nodeConnectors, nodes, threads, latencies, throughputs);
	}

	void start() {
		threads.forEach(Thread::start);
	}

	void stop() {
		threads.stream()
				.peek(Thread::interrupt)
				.forEach(thread -> {
					try {
						thread.join();
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				});
	}

	public List<NodeConnector> getNodeConnectors() {
		return nodeConnectors;
	}

	public long[] getLatencies() {
		return latencies;
	}

	public long[] getThroughputs() {
		return throughputs;
	}
}
