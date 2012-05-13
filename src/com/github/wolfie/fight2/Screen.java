package com.github.wolfie.fight2;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.mojang.mojam.screen.Bitmap;

public class Screen extends Bitmap {

	private final BufferedImage image;

	public Screen(final int width, final int height) {
		super(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	public BufferedImage getImage() {
		return image;
	}

	public void fill(final double x, final double y, final double w,
			final double h, final int color) {
		fill((int) x, (int) y, (int) w, (int) h, color);
	}
}
