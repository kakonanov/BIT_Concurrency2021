package com.sbertech.concurrency.hw6;

import com.sbertech.concurrency.hw6.NodeConnector.NodeConnector;
import org.junit.jupiter.api.Test;

import java.util.List;

class TokenRingTest {

	@Test
	public void test() throws InterruptedException {
		TokenRing tokenRing = TokenRing.create(4, 8);
		List<NodeConnector> nodeConnectorList = tokenRing.getNodeConnectors();
		tokenRing.start();
		nodeConnectorList.get(0).put(new Token(0, 0, 3));
		nodeConnectorList.get(1).put(new Token(1, 0, 3));
		tokenRing.stop();
	}

}