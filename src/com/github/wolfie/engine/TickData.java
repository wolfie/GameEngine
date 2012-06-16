package com.github.wolfie.engine;

public class TickData {
	public final KeyData keys;
	public final MouseData mouseData;

	public TickData(final KeyData keys, final MouseData mouseData) {
		this.keys = keys;
		this.mouseData = mouseData;
	}
}
