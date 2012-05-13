package com.github.wolfie.fight2;

import java.util.ArrayList;
import java.util.List;

import com.github.wolfie.fight2.objects.Colliding;
import com.github.wolfie.fight2.objects.CollisionBox;
import com.github.wolfie.fight2.objects.GameObject;
import com.github.wolfie.fight2.objects.Ground;
import com.github.wolfie.fight2.objects.Player;

public class GameScreen implements GuiElement {

	private final List<GameObject> objs = new ArrayList<>();
	private final List<Colliding> collidings = new ArrayList<>();

	public GameScreen() {
		add(new Ground(50));
		add(new Player(10, 10));
		add(new CollisionBox(0, 0, 0, Fight2Canvas.FIGHT_HEIGHT));
		add(new CollisionBox(Fight2Canvas.FIGHT_WIDTH, 0, 0,
				Fight2Canvas.FIGHT_HEIGHT));
		add(new CollisionBox(50, 50, 10, 10) {
			@Override
			public void render(final Screen screen) {
				screen.fill(50, 50, 10, 10, 0xff0000ff);
			}
		});
		add(new CollisionBox(0, Fight2Canvas.FIGHT_HEIGHT - 100, 10, 10) {
			@Override
			public void render(final Screen screen) {
				screen.fill(0, Fight2Canvas.FIGHT_HEIGHT - 100, 10, 10,
						0xff0000ff);
			}
		});
	}

	private void add(final GameObject obj) {
		objs.add(obj);
		if (obj instanceof Colliding) {
			collidings.add((Colliding) obj);
		}
	}

	@Override
	public void render(final Screen screen) {
		screen.fill(0, 0, Fight2Canvas.FIGHT_WIDTH, Fight2Canvas.FIGHT_HEIGHT,
				0xffFFFFFF);

		for (final GameObject obj : objs) {
			obj.render(screen);
		}
	}

	@Override
	public void tick(final long msSinceLastTick, final TickData tickData) {
		if (Fight2Canvas.instance.keyData.esc.wasPressed()) {
			Fight2Canvas.instance.pauseGame();
		}

		for (final GameObject obj : objs) {
			obj.tick(msSinceLastTick, tickData);
		}

		for (int i = 0; i < collidings.size() - 1; i++) {
			for (int j = i + 1; j < collidings.size(); j++) {
				final Colliding coll1 = collidings.get(i);
				final Colliding coll2 = collidings.get(j);

				if (coll1 instanceof GameObject && coll2 instanceof GameObject) {
					final GameObject rect1 = (GameObject) coll1;
					final GameObject rect2 = (GameObject) coll2;
					if (rect1.overlaps(rect2)) {
						coll1.handleCollision(rect2);
						coll2.handleCollision(rect1);
					}
				}
			}
		}
	}

}
