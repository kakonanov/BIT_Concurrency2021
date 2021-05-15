package com.sbertech.concurrency.hw6;

public class Token {
	private final int id;
	private final int srcNumber;
	private final int dstNumber;
	private long creationTime;



	public Token(int id, int srcNumber, int dstNumber) {
		this.id = id;
		this.srcNumber = srcNumber;
		this.dstNumber = dstNumber;
	}

	public int getId() {
		return id;
	}

	public int getDstNumber() {
		return dstNumber;
	}

	public int getSrcNumber() {
		return srcNumber;
	}

	public void setCreationTime(int creationTime) {
		this.creationTime = creationTime;
	}
}
