package com.mojang.mojam.screen;

public class Rect {
	public double topLeftX, topLeftY;
	public double bottomRightX, bottomRightY;
	public final double width, height;

	public Rect(final double x, final double y, final double w, final double h) {
		topLeftX = x;
		topLeftY = y;
		width = w;
		height = h;
		bottomRightX = topLeftX + width;
		bottomRightY = topLeftY + height;
	}

	public boolean overlaps(final Rect rect) {
		return overlaps(this, rect);
	}

	public static boolean overlaps(final Rect rect1, final Rect rect2) {
		final boolean xCollides = overlaps(rect1.topLeftX, rect1.topLeftX
				+ rect1.width, rect2.topLeftX, rect2.topLeftX + rect2.width);
		final boolean yCollides = overlaps(rect1.topLeftY, rect1.topLeftY
				+ rect1.height, rect2.topLeftY, rect2.topLeftY + rect2.height);
		return xCollides && yCollides;
	}

	private static boolean overlaps(final double start1, final double end1,
			final double start2, final double end2) {
		return start1 < end2 && start2 < end1;
	}
}
