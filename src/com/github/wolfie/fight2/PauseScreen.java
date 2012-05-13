package com.github.wolfie.fight2;

import java.util.Stack;

public class PauseScreen implements GuiElement {

	private boolean paused;

	@Override
	public void render(final Screen screen) {
		final Stack<GuiElement> guiStack = Fight2Canvas.instance.guiStack;
		final GuiElement previousScreen = guiStack.get(guiStack.size() - 2);
		previousScreen.render(screen);
		screen.alphaFill(0, 0, Fight2Canvas.FIGHT_WIDTH,
				Fight2Canvas.FIGHT_HEIGHT, 0xFF000000, 0x30);
	}

	@Override
	public void tick(final long msSinceLastTick, final TickData tickData) {
		if (Fight2Canvas.instance.keyData.esc.wasPressed()) {
			Fight2Canvas.instance.unpauseGame();
		}
	}

	public void pause() {
		paused = true;
	}

	public void unpause() {
		paused = false;
	}

	public boolean isPaused() {
		return paused;
	}

}
