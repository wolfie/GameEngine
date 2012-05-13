package com.github.wolfie.fight2;

public interface GuiElement {
	void render(Screen screen);

	void tick(long msBetweenTicks, TickData tickData);
}
