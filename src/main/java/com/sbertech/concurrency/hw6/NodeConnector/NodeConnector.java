package com.sbertech.concurrency.hw6.NodeConnector;

import com.sbertech.concurrency.hw6.Token;

public interface NodeConnector {

	void put(Token token) throws InterruptedException;

	Token take() throws InterruptedException;
}
