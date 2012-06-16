package com.github.wolfie.engine.level;

import com.mojang.mojam.screen.Bitmap;

public interface Tile {
	public interface BottomImpassableTile {
	}

	public interface TopImpassableTile {
	}

	public interface RightImpassableTile {
	}

	public interface LeftImpassableTile {
	}

	public interface ImpassableTile extends LeftImpassableTile,
			RightImpassableTile, TopImpassableTile, BottomImpassableTile {
	}

	public abstract Bitmap getBitmap();
}
