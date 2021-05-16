package com.sbertech.concurrency.hw6;

public class Token {
	private final int id;
	private long time = 0;

	public Token(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "Token{" +
				"id=" + id +
				", time=" + time +
				'}';
	}


}
