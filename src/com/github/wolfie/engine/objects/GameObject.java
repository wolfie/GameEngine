package com.github.wolfie.engine.objects;

import com.github.wolfie.engine.EngineCanvas;
import com.github.wolfie.engine.GuiElement;
import com.github.wolfie.engine.TickData;
import com.github.wolfie.engine.Util;
import com.mojang.mojam.screen.Rect;

public abstract class GameObject extends Rect implements GuiElement {

	public double velocityXPPS = 0.0d;
	public double velocityYPPS = 0.0d;
	public double gravity = EngineCanvas.getConfig().getGravityPPSS();

	public GameObject(final double x, final double y, final double w,
			final double h) {
		super(x, y, w, h);
	}

	public final void moveTo(final double x, final double y) {
		if (x != topLeftX || y != topLeftY) {
			final double deltaX = x - topLeftX;
			final double deltaY = y - topLeftY;
			topLeftX += deltaX;
			topLeftY += deltaY;
			bottomRightX += deltaX;
			bottomRightY += deltaY;
		}
	}

	@Override
	public void tick(final long nsBetweenTicks, final TickData tickData) {

		final double velocityX = getVelocityX(nsBetweenTicks);
		final double velocityY = getVelocityY(nsBetweenTicks);
		moveX(velocityX);
		moveY(velocityY);

		// initial speed + acceleration * time = final speed
		velocityYPPS += gravity * Util.nanoSecondsToSeconds(nsBetweenTicks);
		_tick(nsBetweenTicks, tickData);
	}

	protected abstract void moveX(final double velocityX);

	protected abstract void moveY(final double velocityY);

	private double getVelocityY(final long nsBetweenTicks) {
		return velocityYPPS * Util.nanoSecondsToSeconds(nsBetweenTicks);
	}

	private double getVelocityX(final long nsBetweenTicks) {
		return velocityXPPS * Util.nanoSecondsToSeconds(nsBetweenTicks);
	}

	protected abstract void _tick(long nsBetweenTicks, TickData tickData);
}
