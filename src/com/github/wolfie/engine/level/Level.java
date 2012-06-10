package com.github.wolfie.engine.level;

public class Level {
	public final int width;
	public final int height;
	private final AbstractTile[] tiles;

	public Level(final int width, final int height, final AbstractTile[] tiles) {
		if (tiles.length != width * height) {
			throw new LevelLoadingException(
					"Amount of tiles doesn't add up with the height and width");
		}
		this.width = width;
		this.height = height;
		this.tiles = tiles;
	}

	public AbstractTile getTile(final int x, final int y) {
		return tiles[x + y * width];
	}
}
