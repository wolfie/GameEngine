package com.github.wolfie.fight2.objects;

import com.github.wolfie.fight2.Fight2Canvas;
import com.github.wolfie.fight2.GuiElement;
import com.github.wolfie.fight2.TickData;
import com.mojang.mojam.screen.Rect;

public abstract class GameObject extends Rect implements GuiElement {

	protected boolean isOnGround = false;
	public double velocityXPPS = 0.0d;
	public double velocityYPPS = 0.0d;
	public double gravity = Fight2Canvas.GRAVITY_PPSS;

	public double prevTopLeftX;
	public double prevTopLeftY;

	public GameObject(final double x, final double y, final double w,
			final double h) {
		super(x, y, w, h);
	}

	public void moveTo(final double x, final double y) {
		if (x != topLeftX || y != topLeftY) {
			prevTopLeftX = topLeftX;
			prevTopLeftY = topLeftY;

			final double deltaX = x - topLeftX;
			final double deltaY = y - topLeftY;
			topLeftX += deltaX;
			topLeftY += deltaY;
			bottomRightX += deltaX;
			bottomRightY += deltaY;
		}
	}

	@Override
	public void tick(final long msBetweenTicks, final TickData tickData) {
		_tick(msBetweenTicks, tickData);
		if (!isOnGround) {
			// initial speed + acceleration * time = final speed
			velocityYPPS += gravity * msBetweenTicks / 1000.0d;
		}

		final double velocityX = getVelocityX(msBetweenTicks);
		final double velocityY = getVelocityY(msBetweenTicks);
		moveTo(topLeftX + velocityX, topLeftY + velocityY);
	}

	private double getVelocityY(final long msBetweenTicks) {
		return velocityYPPS * msBetweenTicks / 1000;
	}

	private double getVelocityX(final long msBetweenTicks) {
		return velocityXPPS * msBetweenTicks / 1000;
	}

	protected abstract void _tick(long msBetweenTicks, TickData tickData);

	public void isOnGround() {
		isOnGround = true;
		velocityYPPS = 0;
	}

}
