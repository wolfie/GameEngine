package com.github.wolfie.engine;

import com.mojang.mojam.screen.Bitmap;

public interface GuiElement {
	Bitmap getBitmap(long nsSinceLastFrame);

	void tick(long nsBetweenTicks, TickData tickData);
}
