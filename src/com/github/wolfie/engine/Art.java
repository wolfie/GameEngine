package com.github.wolfie.engine;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mojang.mojam.screen.Bitmap;

public class Art {
	/**
	 * Load a bitmap resource by name
	 * 
	 * @param string
	 *            Resource name
	 * @return Bitmap on success, null on error
	 */
	protected static Bitmap load(final String string) {
		try {
			final BufferedImage bi = ImageIO
					.read(Art.class.getResource(string));

			final int w = bi.getWidth();
			final int h = bi.getHeight();

			final Bitmap result = new Bitmap(w, h);
			bi.getRGB(0, 0, w, h, result.pixels, 0, w);

			return result;
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Return the bitmaps for a given piece of art, cut out from a sheet
	 * 
	 * @param string
	 *            Art piece name
	 * @param w
	 *            Width of a single bitmap
	 * @param h
	 *            Height of a single bitmap
	 * @return Bitmap array
	 */
	public static Bitmap[][] cut(final String string, final int w, final int h) {
		return cut(string, w, h, 0, 0);
	}

	/**
	 * Return the bitmaps for a given piece of art, cut out from a sheet
	 * 
	 * @param string
	 *            Art piece name
	 * @param w
	 *            Width of a single bitmap
	 * @param h
	 *            Height of a single bitmap
	 * @param bx
	 * @param by
	 * @return Bitmap array
	 */
	private static Bitmap[][] cut(final String string, final int w,
			final int h, final int bx, final int by) {
		try {
			final BufferedImage bi = ImageIO
					.read(Art.class.getResource(string));

			final int xTiles = (bi.getWidth() - bx) / w;
			final int yTiles = (bi.getHeight() - by) / h;

			final Bitmap[][] result = new Bitmap[xTiles][yTiles];

			for (int x = 0; x < xTiles; x++) {
				for (int y = 0; y < yTiles; y++) {
					result[x][y] = new Bitmap(w, h);
					bi.getRGB(bx + x * w, by + y * h, w, h,
							result[x][y].pixels, 0, w);
				}
			}

			return result;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
