package com.github.wolfie.fight2.objects;

import com.github.wolfie.fight2.Screen;
import com.github.wolfie.fight2.TickData;

public class CollisionBox extends GameObject implements Colliding {

	public CollisionBox(final double x, final double y, final double w,
			final double h) {
		super(x, y, w, h);
	}

	@Override
	public void handleCollision(final GameObject obj) {
		if (obj.prevTopLeftY < obj.topLeftY
				&& (obj.prevTopLeftY + obj.height) <= topLeftY) {
			// he's hit the top, make him walk on it
			obj.isOnGround();
			obj.moveTo(obj.topLeftX, topLeftY - obj.height);
			System.out.println("top");
			System.out.println(obj.prevTopLeftY + "->" + obj.topLeftY);
		}

		else if (obj.prevTopLeftY > obj.topLeftY
				&& obj.prevTopLeftY >= bottomRightY) {
			// hit the bottom, bonk the head
			obj.velocityYPPS = 0;
			obj.moveTo(obj.topLeftX, topLeftY + height);
			System.out.println(obj.prevTopLeftX + "->" + obj.topLeftX);
			System.out.println("bottom");
			System.out.println(obj.prevTopLeftY + "->" + obj.topLeftY);
		}

		else if (obj.prevTopLeftX < obj.topLeftX
				&& obj.prevTopLeftX + obj.height <= topLeftX) {
			obj.velocityXPPS = 0;
			obj.moveTo(topLeftX - obj.width, obj.topLeftY);
			System.out.println("left");
			System.out.println(obj.prevTopLeftX + "->" + obj.topLeftX);
		}

		else if (obj.prevTopLeftX > obj.topLeftX
				&& obj.prevTopLeftX >= bottomRightX) {
			obj.velocityXPPS = 0;
			obj.moveTo(topLeftX + width, obj.topLeftY);
			System.out.println("right");
			System.out.println(obj.prevTopLeftX + "->" + obj.topLeftX);
		}

		else {
			System.err.println("DERP");
		}
	}

	@Override
	public void render(final Screen screen) {
	}

	@Override
	public void tick(final long msBetweenTicks, final TickData tickData) {
	}

	@Override
	protected void _tick(final long msBetweenTicks, final TickData tickData) {
	}

}
