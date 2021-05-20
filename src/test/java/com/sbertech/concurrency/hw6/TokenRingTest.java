package com.sbertech.concurrency.hw6;

import com.sbertech.concurrency.hw6.NodeConnector.NodeConnector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class TokenRingTest {
	private static final int latencyArraySize = 100;
	private static final long millisForWork = 10_000; //milliseconds
	private static final long periodTime = 500; // milliseconds
	private static final int throughputArraySize = (int) ((millisForWork - 3_000) / periodTime);

	private static final Path latencyPath = Paths.get("src/test/resources/latencyDepPockets.csv");
	private static final Path throughputPath = Paths.get("src/test/resources/throughputDepPockets.csv");

	private static final CSVWriter latencyWriter = new CSVWriter(latencyPath);
	private static final CSVWriter throughputWriter = new CSVWriter(throughputPath);




	@BeforeAll
	static void before() throws InterruptedException, FileNotFoundException {
		int size = 2;
		int queueSize = 2;
		TokenRing tokenRing = TokenRing.create(size, queueSize, latencyArraySize, throughputArraySize, periodTime);
		List<NodeConnector> nodeConnectorList = tokenRing.getNodeConnectors();
		nodeConnectorList.get(0).put(new Token(0));
		tokenRing.start();
		Thread.sleep(5_000);
		tokenRing.stop();
	}


//	@ParameterizedTest
//	@ValueSource(ints = {2, 3, 4, 5, 6, 7})
//	public void testNumNodes(int size) throws InterruptedException {
//		double load = 0.2;
//		int queueSize = size;
//		testTokenRing(load, size, queueSize, (int) Math.ceil(size * queueSize * load));
//	}

//	@ParameterizedTest
//	@ValueSource(ints = {2, 3, 4})
//	public void testLowLoad(int size) throws InterruptedException {
//		double load = 0.5;
//		for (int queueSize = 2; queueSize <= size + 5; ++queueSize) {
//			testTokenRing(load, size, queueSize, (int) Math.ceil(size * load));
//		}
//	}
//
//	@ParameterizedTest
//	@ValueSource(ints = {2, 3, 4})
//	public void testMediumLoad(int size) throws InterruptedException {
//		double load = 1;
//		for (int queueSize = 2; queueSize <= size + 5; ++queueSize) {
//			testTokenRing(load, size, queueSize, (int) Math.ceil(size * load));
//		}
//	}
//
//	@ParameterizedTest
//	@ValueSource(ints = {2, 3, 4})
//	public void testHighLoad(int size) throws InterruptedException {
//		double load = 0.8;
//		for (int queueSize = 2; queueSize <= size + 5; ++queueSize) {
//			testTokenRing(load, size, queueSize, (int) Math.ceil(size * 2 * load));
//		}
//	}


	@ParameterizedTest
	@ValueSource(ints = {4, 8, 12, 20, 28})
	public void testHighLoad(int queueSize) throws InterruptedException {
		double load = 0.8;
		int size = 4;
		for (int pocketNum = 3; pocketNum < size * queueSize; pocketNum += 4)
			testTokenRing(load, size, queueSize, pocketNum);
	}

	public void testTokenRing(double load, int size, int queueSize, int pocketNum) throws InterruptedException {
		TokenRing tokenRing = TokenRing.create(size, queueSize, latencyArraySize, throughputArraySize, periodTime);
		List<NodeConnector> nodeConnectorList = tokenRing.getNodeConnectors();
		for (int i = 0; i < pocketNum; ++i) {
			nodeConnectorList.get(i % size).put(new Token(i));
		}
		tokenRing.start();
		Thread.sleep(millisForWork);
		tokenRing.stop();

		latencyWriter.write(load, size, queueSize, pocketNum, tokenRing.getLatencies());
		throughputWriter.write(load, size, queueSize, pocketNum, tokenRing.getThroughputs());
	}

}