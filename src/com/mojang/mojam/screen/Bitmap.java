package com.mojang.mojam.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;

public class Bitmap {
	public int w, h;
	public int[] pixels;

	public Bitmap(final int w, final int h) {
		this.w = w;
		this.h = h;
		pixels = new int[w * h];
	}

	public Bitmap(final int w, final int h, final int[] pixels) {
		this.w = w;
		this.h = h;
		this.pixels = pixels;
	}

	public Bitmap(final Dimension dimension) {
		this(dimension.width, dimension.height);
	}

	public Bitmap(final int[][] pixels2D) {
		w = pixels2D.length;
		if (w > 0) {
			h = pixels2D[0].length;
			pixels = new int[w * h];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					pixels[y * w + x] = pixels2D[x][y];
				}
			}
		} else {
			h = 0;
			pixels = new int[0];
		}
	}

	public Bitmap copy() {
		final Bitmap rValue = new Bitmap(this.w, this.h);
		rValue.pixels = Arrays.copyOf(this.pixels, this.pixels.length);
		return rValue;
	}

	public void clear(final int color) {
		Arrays.fill(pixels, color);
	}

	public void blit(final Bitmap bitmap, final int x, final int y,
			final Alignment align) {
		final int alignedX;
		if (align.isLeft()) {
			alignedX = x;
		} else if (align.isCenter()) {
			alignedX = w / 2 - bitmap.w / 2;
		} else {
			alignedX = w - bitmap.w - x;
		}

		final int alignedY;
		if (align.isTop()) {
			alignedY = y;
		} else if (align.isMiddle()) {
			alignedY = h / 2 - bitmap.h / 2;
		} else {
			alignedY = h - bitmap.h - y;
		}
		blit(bitmap, alignedX, alignedY);
	}

	public void blit(final Bitmap bitmap, final int x, final int y) {
		final Rect blitArea = new Rect(x, y, bitmap.w, bitmap.h);
		adjustBlitArea(blitArea);
		final int blitWidth = (int) blitArea.bottomRightX
				- (int) blitArea.topLeftX;

		for (int yy = (int) blitArea.topLeftY; yy < blitArea.bottomRightY; yy++) {
			int tp = yy * w + (int) blitArea.topLeftX;
			final int sp = (yy - y) * bitmap.w + (int) (blitArea.topLeftX - x);
			tp -= sp;
			for (int xx = sp; xx < sp + blitWidth; xx++) {
				final int col = bitmap.pixels[xx];
				final int alpha = (col >> 24) & 0xff;

				if (alpha == 255) {
					pixels[tp + xx] = col;
				} else {
					pixels[tp + xx] = blendPixels(pixels[tp + xx], col);
				}
			}
		}
	}

	public static int blendPixels(final int backgroundColor,
			final int pixelToBlendColor) {

		final int alpha_blend = (pixelToBlendColor >> 24) & 0xff;
		final int alpha_background = 256 - alpha_blend;

		final int rr = backgroundColor & 0xff0000;
		final int gg = backgroundColor & 0xff00;
		final int bb = backgroundColor & 0xff;

		int r = pixelToBlendColor & 0xff0000;
		int g = pixelToBlendColor & 0xff00;
		int b = pixelToBlendColor & 0xff;

		r = ((r * alpha_blend + rr * alpha_background) >> 8) & 0xff0000;
		g = ((g * alpha_blend + gg * alpha_background) >> 8) & 0xff00;
		b = ((b * alpha_blend + bb * alpha_background) >> 8) & 0xff;

		return backgroundColor & 0xff000000 | r | g | b;
	}

	public void blit(final Bitmap bitmap, final int x, final int y,
			final int width, final int height) {

		final Rect blitArea = new Rect(x, y, width, height);
		adjustBlitArea(blitArea);
		final int blitWidth = (int) blitArea.bottomRightX
				- (int) blitArea.topLeftX;

		for (int yy = (int) blitArea.topLeftY; yy < blitArea.bottomRightY; yy++) {
			int tp = yy * w + (int) blitArea.topLeftX;
			final int sp = (yy - y) * bitmap.w + (int) (blitArea.topLeftX - x);
			tp -= sp;
			for (int xx = sp; xx < sp + blitWidth; xx++) {
				final int col = bitmap.pixels[xx];
				final int alpha = (col >> 24) & 0xff;

				if (alpha == 255) {
					pixels[tp + xx] = col;
				} else {
					pixels[tp + xx] = blendPixels(pixels[tp + xx], col);
				}
			}
		}
	}

	/**
	 * Draws a Bitmap semi-transparent
	 * 
	 * @param bitmap
	 *            image to draw
	 * @param x
	 *            position on screen
	 * @param y
	 *            position on screen
	 * @param alpha
	 *            range from 0x00 (transparent) to 0xff (opaque)
	 */
	public void alphaBlit(final Bitmap bitmap, final int x, final int y,
			final int alpha) {

		if (alpha == 255) {
			this.blit(bitmap, x, y);
			return;
		}

		final Rect blitArea = new Rect(x, y, bitmap.w, bitmap.h);
		adjustBlitArea(blitArea);

		final int blitWidth = (int) blitArea.bottomRightX
				- (int) blitArea.topLeftX;

		for (int yy = (int) blitArea.topLeftY; yy < blitArea.bottomRightY; yy++) {
			final int tp = yy * w + (int) blitArea.topLeftX;
			final int sp = (yy - y) * bitmap.w + (int) (blitArea.topLeftX - x);
			for (int xx = 0; xx < blitWidth; xx++) {
				int col = bitmap.pixels[sp + xx];
				if (col < 0) {

					final int r = (col & 0xff0000);
					final int g = (col & 0xff00);
					final int b = (col & 0xff);
					col = (alpha << 24) | r | g | b;
					final int color = pixels[tp + xx];
					pixels[tp + xx] = this.blendPixels(color, col);
				}
			}
		}
	}

	public void colorBlit(final Bitmap bitmap, final int x, final int y,
			final int color) {

		final Rect blitArea = new Rect(x, y, bitmap.w, bitmap.h);
		adjustBlitArea(blitArea);

		final int blitWidth = (int) blitArea.bottomRightX
				- (int) blitArea.topLeftX;

		final int a2 = (color >> 24) & 0xff;
		final int a1 = 256 - a2;

		final int rr = color & 0xff0000;
		final int gg = color & 0xff00;
		final int bb = color & 0xff;

		for (int yy = (int) blitArea.topLeftY; yy < (int) blitArea.bottomRightY; yy++) {
			final int tp = yy * w + (int) blitArea.topLeftX;
			final int sp = (yy - y) * bitmap.w + (int) (blitArea.topLeftX - x);
			for (int xx = 0; xx < blitWidth; xx++) {
				final int col = bitmap.pixels[sp + xx];
				if (col < 0) {
					int r = (col & 0xff0000);
					int g = (col & 0xff00);
					int b = (col & 0xff);

					r = ((r * a1 + rr * a2) >> 8) & 0xff0000;
					g = ((g * a1 + gg * a2) >> 8) & 0xff00;
					b = ((b * a1 + bb * a2) >> 8) & 0xff;
					pixels[tp + xx] = 0xff000000 | r | g | b;
				}
			}
		}
	}

	/**
	 * Fills semi-transparent region on screen
	 * 
	 * @param x
	 *            position on screen
	 * @param y
	 *            position on screen
	 * @param width
	 *            of the region
	 * @param height
	 *            of the region
	 * @param color
	 *            to fill the region
	 * @param alpha
	 *            range from 0x00 (transparent) to 0xff (opaque)
	 */
	public void alphaFill(final int x, final int y, final int width,
			final int height, final int color, final int alpha) {

		if (alpha == 255) {
			this.fill(x, y, width, height, color);
			return;
		}

		final Bitmap bmp = new Bitmap(width, height);
		bmp.fill(0, 0, width, height, color);

		this.alphaBlit(bmp, x, y, alpha);
	}

	public void fill(final int x, final int y, final int width,
			final int height, final int color) {

		final Rect blitArea = new Rect(x, y, width, height);
		adjustBlitArea(blitArea);

		final int blitWidth = (int) blitArea.bottomRightX
				- (int) blitArea.topLeftX;

		for (int yy = (int) blitArea.topLeftY; yy < blitArea.bottomRightY; yy++) {
			final int tp = yy * w + (int) blitArea.topLeftX;
			for (int xx = 0; xx < blitWidth; xx++) {
				pixels[tp + xx] = color;
			}
		}
	}

	private void adjustBlitArea(final Rect blitArea) {

		if (blitArea.topLeftX < 0) {
			blitArea.topLeftX = 0;
		}
		if (blitArea.topLeftY < 0) {
			blitArea.topLeftY = 0;
		}
		if (blitArea.bottomRightX > w) {
			blitArea.bottomRightX = w;
		}
		if (blitArea.bottomRightY > h) {
			blitArea.bottomRightY = h;
		}
	}

	public void rectangle(final int x, final int y, final int bw, final int bh,
			final int color) {
		int x0 = x;
		int x1 = x + bw;
		int y0 = y;
		int y1 = y + bh;
		if (x0 < 0) {
			x0 = 0;
		}
		if (y0 < 0) {
			y0 = 0;
		}
		if (x1 > w) {
			x1 = w;
		}
		if (y1 > h) {
			y1 = h;
		}

		for (int yy = y0; yy < y1; yy++) {
			setPixel(x0, yy, color);
			setPixel(x1 - 1, yy, color);
		}

		for (int xx = x0; xx < x1; xx++) {
			setPixel(xx, y0, color);
			setPixel(xx, y1 - 1, color);
		}
	}

	public void setPixel(final int x, final int y, final int color) {
		pixels[x + y * w] = color;
	}

	public int getPixel(final int x, final int y) {
		return pixels[x + y * w];
	}

	public static Bitmap rectangleBitmap(final int x, final int y,
			final int x2, final int y2, final int color) {
		final Bitmap rect = new Bitmap(x2, y2);
		rect.rectangle(x, y, x2, y2, color);
		return rect;
	}

	public static Bitmap rangeBitmap(final int radius, final int color) {
		final Bitmap circle = new Bitmap(radius * 2 + 100, radius * 2 + 100);

		circle.circleFill(radius, radius, radius, color);
		return circle;
	}

	public static Bitmap tooltipBitmap(final int width, final int height) {
		final int cRadius = 3;
		final int color = Color.black.getRGB();
		final Bitmap tooltip = new Bitmap(width + 3, height + 3);
		tooltip.fill(0, cRadius, width, height - 2 * cRadius, color);
		tooltip.fill(cRadius, 0, width - 2 * cRadius, height, color);
		// draw corner circles
		tooltip.circleFill(cRadius, cRadius, cRadius, color);
		tooltip.circleFill(width - cRadius, cRadius, cRadius, color);
		tooltip.circleFill(width - cRadius, height - cRadius, cRadius, color);
		tooltip.circleFill(cRadius, height - cRadius, cRadius, color);

		return tooltip;
	}

	public void circle(final int centerX, final int centerY, final int radius,
			final int color) {
		int d = 3 - (2 * radius);
		int x = 0;
		int y = radius;

		do {
			setPixel(centerX + x, centerY + y, color);
			setPixel(centerX + x, centerY - y, color);
			setPixel(centerX - x, centerY + y, color);
			setPixel(centerX - x, centerY - y, color);
			setPixel(centerX + y, centerY + x, color);
			setPixel(centerX + y, centerY - x, color);
			setPixel(centerX - y, centerY + x, color);
			setPixel(centerX - y, centerY - x, color);

			if (d < 0) {
				d = d + (4 * x) + 6;
			} else {
				d = d + 4 * (x - y) + 10;
				y--;
			}
			x++;
		} while (x <= y);
	}

	public void circleFill(final int centerX, final int centerY,
			final int radius, final int color) {
		int d = 3 - (2 * radius);
		int x = 0;
		int y = radius;

		do {
			horizonalLine(centerX + x, centerX - x, centerY + y, color);
			horizonalLine(centerX + x, centerX - x, centerY - y, color);
			horizonalLine(centerX + y, centerX - y, centerY + x, color);
			horizonalLine(centerX + y, centerX - y, centerY - x, color);

			if (d < 0) {
				d = d + (4 * x) + 6;
			} else {
				d = d + 4 * (x - y) + 10;
				y--;
			}
			x++;
		} while (x <= y);
	}

	public void horizonalLine(int x1, int x2, final int y, final int color) {
		if (x1 > x2) {
			final int xx = x1;
			x1 = x2;
			x2 = xx;
		}

		for (int xx = x1; xx <= x2; xx++) {
			setPixel(xx, y, color);
		}
	}

	public static Bitmap shrink(final Bitmap bitmap) {
		final Bitmap newbmp = new Bitmap(bitmap.w / 2, bitmap.h / 2);
		final int[] pix = bitmap.pixels;
		int blarg = 0;
		for (int i = 0; i < pix.length; i++) {
			if (blarg >= newbmp.pixels.length) {
				break;
			}
			if (i % 2 == 0) {
				newbmp.pixels[blarg] = pix[i];
				blarg++;
			}
			if (i % bitmap.w == 0) {
				i += bitmap.w;
			}
		}

		return newbmp;
	}

	public static Bitmap scaleBitmap(final Bitmap bitmap, final int width,
			final int height) {
		final Bitmap scaledBitmap = new Bitmap(width, height);

		final int scaleRatioWidth = ((bitmap.w << 16) / width);
		final int scaleRatioHeight = ((bitmap.h << 16) / height);

		int i = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				scaledBitmap.pixels[i++] = bitmap.pixels[(bitmap.w * ((y * scaleRatioHeight) >> 16))
						+ ((x * scaleRatioWidth) >> 16)];
			}
		}

		return scaledBitmap;
	}

	public Bitmap flipHorizontally() {
		final Bitmap bitmap = new Bitmap(w, h);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				bitmap.pixels[y * w + (w - x - 1)] = pixels[y * w + x];
			}
		}

		return bitmap;
	}

	public Bitmap blend(final int color) {
		final Bitmap copy = copy();
		for (int i = 0; i < copy.pixels.length; i++) {
			copy.pixels[i] = blendPixels(copy.pixels[i], color);
		}
		return copy;
	}

	public void blendSelf(final int color) {
		pixels = blend(color).pixels;
	}
}