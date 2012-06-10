package com.github.wolfie.engine;

import com.mojang.mojam.screen.Bitmap;

public abstract class GameScreen implements GuiElement {
	public final int width;
	public final int height;
	protected final Bitmap bitmap;

	protected GameScreen(final int width, final int height) {
		this.width = width;
		this.height = height;
		bitmap = new Bitmap(width, height);
	}
}
