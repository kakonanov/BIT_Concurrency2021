package com.sbertech.concurrency.hw6;

import com.sbertech.concurrency.hw6.NodeConnector.NodeConnector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

class TokenRingTest {
	private static final int latencyArraySize = 100;
	private static final long millisForWork = 10_000; //milliseconds
	private static final long periodTime = 500; // milliseconds
	private static final int throughputArraySize = (int) ((millisForWork - 2_000) / periodTime);


	@BeforeAll
	static void before() throws InterruptedException {
		int size = 2;
		int queueSize = 2;
		TokenRing tokenRing = TokenRing.create(size, queueSize, latencyArraySize, throughputArraySize, periodTime);
		List<NodeConnector> nodeConnectorList = tokenRing.getNodeConnectors();
		nodeConnectorList.get(0).put(new Token(0));
		tokenRing.start();
		Thread.sleep(millisForWork);
		tokenRing.stop();
	}

	@ParameterizedTest
	@ValueSource(doubles = {0.15, 0.5, 0.8})
	public void test(double load) throws InterruptedException {
		System.out.println("Load: " + load);
//		for (int size = 2; size <= 6; ++size) {
//			for (int queueSize = 2; queueSize <= 6; ++queueSize) {
//				testTokenRing(size, queueSize, size * queueSize * load);
//			}
//		}
		testTokenRingToCheck(3, 4, 8);
		System.out.println("------------------------------\n\n");
	}

	public void testTokenRingToCheck(int size, int queueSize, double pocketNum) throws InterruptedException {
		System.out.println("Size: " + size + " , queueSize: " +  queueSize);
		TokenRing tokenRing = TokenRing.create(size, queueSize, latencyArraySize, throughputArraySize, periodTime);
		List<NodeConnector> nodeConnectorList = tokenRing.getNodeConnectors();
		for (int i = 0; i < pocketNum; ++i) {
			nodeConnectorList.get(i % size).put(new Token(i));
		}
		tokenRing.start();
		Thread.sleep(millisForWork);
		tokenRing.stop();
		System.out.println(Arrays.toString(tokenRing.getLatencies()));
		System.out.println(Arrays.toString(tokenRing.getThroughputs()));
	}

	public void testTokenRing(int size, int queueSize, double pocketNum) throws InterruptedException {
		System.out.println("Size: " + size + " , queueSize: " +  queueSize);
		TokenRing tokenRing = TokenRing.create(size, queueSize, latencyArraySize, throughputArraySize, periodTime);
		List<NodeConnector> nodeConnectorList = tokenRing.getNodeConnectors();
		for (int i = 0; i < pocketNum; ++i) {
			nodeConnectorList.get(i % size).put(new Token(i));
		}
		tokenRing.start();
		Thread.sleep(millisForWork);
		tokenRing.stop();
		System.out.println(Arrays.toString(tokenRing.getLatencies()));
		System.out.println(Arrays.toString(tokenRing.getThroughputs()));
	}

}