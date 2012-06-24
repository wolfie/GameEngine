package com.github.wolfie.engine.level;

import java.util.Arrays;

public class Level {
	public final int widthInTiles;
	public final int heightInTiles;
	private final Tile[] tiles;

	public Level(final int width, final int height, final Tile[] tiles) {
		if (tiles.length != width * height) {
			throw new LevelLoadingException(
					"Amount of tiles doesn't add up with the height and width");
		}
		this.widthInTiles = width;
		this.heightInTiles = height;
		this.tiles = tiles;
	}

	public Level(final Level level) {
		widthInTiles = level.widthInTiles;
		heightInTiles = level.heightInTiles;
		tiles = Arrays.copyOf(level.tiles, level.tiles.length);
	}

	public Tile getTile(final int x, final int y) {
		if (x < 0 || x >= widthInTiles || y < 0 || y >= heightInTiles) {
			return null;
		} else {
			return tiles[x + y * widthInTiles];
		}
	}
}
