package com.sbertech.concurrency.hw6;


import com.sbertech.concurrency.hw6.NodeConnector.NodeConnector;
import com.sbertech.concurrency.hw6.node.Node;

public class TokenRingRunnable implements Runnable {
	private final Node node;
	private final NodeConnector nodeConnector;

	public TokenRingRunnable(Node node, NodeConnector nodeConnector) {
		this.node = node;
		this.nodeConnector = nodeConnector;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				node.handleToken(nodeConnector.take());
			} catch (InterruptedException e) {
//				System.out.println("Thread " + Integer.parseInt(Thread.currentThread().getName()) + " is interrupted!!!");
				return;
			}
		}
	}
}
