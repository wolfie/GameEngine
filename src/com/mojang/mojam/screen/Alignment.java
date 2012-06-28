package com.mojang.mojam.screen;

public class Alignment {
	public static final Alignment TOP_LEFT = new Alignment(-1, -1);
	public static final Alignment TOP_CENTER = new Alignment(-1, 0);
	public static final Alignment TOP_RIGHT = new Alignment(-1, 1);
	public static final Alignment MIDDLE_LEFT = new Alignment(0, -1);
	public static final Alignment MIDDLE_CENTER = new Alignment(0, 0);
	public static final Alignment MIDDLE_RIGHT = new Alignment(0, 1);
	public static final Alignment BOTTOM_LEFT = new Alignment(1, -1);
	public static final Alignment BOTTOM_CENTER = new Alignment(1, 0);
	public static final Alignment BOTTOM_RIGHT = new Alignment(1, 1);

	private final int vertical;
	private final int horizontal;

	private Alignment(final int v, final int h) {
		vertical = v;
		horizontal = h;
	}

	public boolean isTop() {
		return vertical < 0;
	}

	public boolean isMiddle() {
		return vertical == 0;
	}

	public boolean isBottom() {
		return vertical > 0;
	}

	public boolean isLeft() {
		return horizontal < 0;
	}

	public boolean isCenter() {
		return horizontal == 0;
	}

	public boolean isRight() {
		return horizontal > 0;
	}
}
