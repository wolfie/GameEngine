package com.github.wolfie.engine.level;

import java.util.Arrays;

public class Level {
	public final int width;
	public final int height;
	private final Tile[] tiles;

	public Level(final int width, final int height, final Tile[] tiles) {
		if (tiles.length != width * height) {
			throw new LevelLoadingException(
					"Amount of tiles doesn't add up with the height and width");
		}
		this.width = width;
		this.height = height;
		this.tiles = tiles;
	}

	public Level(final Level level) {
		width = level.width;
		height = level.height;
		tiles = Arrays.copyOf(level.tiles, level.tiles.length);
	}

	public Tile getTile(final int x, final int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return null;
		} else {
			return tiles[x + y * width];
		}
	}
}
