package com.github.wolfie.engine;

public interface Config {
	String getGameTitle();

	EngineCanvas getGameInstance();

	int getScale();

	int getWidth();

	int getHeight();

	/** Get the gravity, px/s<sup>2</sup> */
	double getGravityPPSS();
}
