package com.sbertech.concurrency.hw6.node;

import com.sbertech.concurrency.hw6.Token;

public interface Node {
	default int getOrderNumber() {return Integer.parseInt(Thread.currentThread().getName());}

	void handleToken(Token token) throws InterruptedException;
}
