package com.github.wolfie.engine;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import com.mojang.mojam.screen.Bitmap;

public class Font {

	public static class CaseInsensitive extends Font {
		public CaseInsensitive(final Bitmap[][] bitmap, final String... letters) {
			super(bitmap, convertToUpper(letters));
		}

		private static String[] convertToUpper(final String[] letters) {
			final String[] ciLetters = new String[letters.length];
			for (int i = 0; i < letters.length; i++) {
				ciLetters[i] = letters[i].toUpperCase();
			}
			return ciLetters;
		}

		@Override
		public Bitmap textToBitmap(final String text) {
			return super.textToBitmap(text.toUpperCase());
		}
	}

	private final Map<Character, Bitmap> charMap = new HashMap<>();
	private final int height;
	private final int width;

	public Font(final Bitmap[][] bitmap, final String... letters) {
		final Bitmap b = bitmap[0][0];
		height = b.h;
		width = b.w;

		for (int i = 0; i < letters.length; i++) {
			final char[] row = letters[i].toCharArray();
			for (int j = 0; j < row.length; j++) {
				charMap.put(row[j], bitmap[j][i]);
			}
		}
	}

	public Bitmap textToBitmap(final String text) {
		final Bitmap bitmap = new Bitmap(calculateRequiredBitmapDimensions(text));

		final String[] lines = text.split("\n");
		for (int i = 0; i < lines.length; i++) {
			final char[] line = lines[i].toCharArray();
			for (int j = 0; j < line.length; j++) {
				final char c = line[j];
				final Bitmap img = charMap.get(c);
				if (img != null) {
					bitmap.blit(img, j * width, i * height);
				}
			}
		}

		return bitmap;
	}

	private Dimension calculateRequiredBitmapDimensions(final String text) {
		int height = 1;
		int width = 0;

		int lineWidth = 0;
		for (final char c : text.toCharArray()) {
			if (c == '\n') {
				width = Math.max(width, lineWidth);
				lineWidth = 0;
				height++;
			} else {
				lineWidth++;
			}

		}

		width = Math.max(width, lineWidth);

		return new Dimension(width * this.width, height * this.height);
	}

}
