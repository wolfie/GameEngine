package com.github.wolfie.engine.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

abstract public class LevelBuilder {
	private PlayerStartTile playerStartTile;

	public Level build() {
		try {
			final BufferedImage image = ImageIO.read(getLevelImage());

			final int width = image.getWidth();
			final int height = image.getHeight();
			final AbstractTile[] tiles = getTiles(image, width, height);
			return new Level(width, height, tiles);
		} catch (final IOException e) {
			throw new LevelLoadingException(e);
		}
	}

	private AbstractTile[] getTiles(final BufferedImage image, final int width,
			final int height) {

		final AbstractTile[] tiles = new AbstractTile[width * height];
		final int[] rgb = new int[width * height];

		image.getRGB(0, 0, width, height, rgb, 0, width);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final int i = x + y * width;
				final AbstractTile tile = getTileForColor(rgb[i]);
				if (tile == null) {
					throw new LevelLoadingException(
							"Trying to load a null tile. That won't work.");
				}
				if (tile instanceof PlayerStartTile) {
					playerStartTile = (PlayerStartTile) tile;
				}
				tile.x = x;
				tile.y = y;
				tiles[i] = tile;
			}
		}

		return tiles;
	}

	public PlayerStartTile getPlayerStartTile() {
		return playerStartTile;
	}

	abstract protected AbstractTile getTileForColor(int hexColor);

	abstract protected URL getLevelImage();
}
