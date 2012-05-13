package com.github.wolfie.fight2.objects;

import com.github.wolfie.fight2.Fight2Canvas;
import com.github.wolfie.fight2.Screen;

public class Ground extends CollisionBox {

	public Ground(final int pixFromBottom) {
		super(0, Fight2Canvas.FIGHT_HEIGHT - pixFromBottom,
				Fight2Canvas.FIGHT_WIDTH, Fight2Canvas.FIGHT_HEIGHT);
	}

	@Override
	public void render(final Screen screen) {
		screen.fill(topLeftX, topLeftY, width, height, 0xFF00FF00);
	}

}
