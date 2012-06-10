package com.github.wolfie.engine.level;

@SuppressWarnings("serial")
public class LevelLoadingException extends RuntimeException {
	public LevelLoadingException(final Throwable e) {
		super(e);
	}

	public LevelLoadingException(final String msg) {
		super(msg);
	}
}