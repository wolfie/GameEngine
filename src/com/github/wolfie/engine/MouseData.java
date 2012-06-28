package com.github.wolfie.engine;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

public class MouseData implements MouseMotionListener, MouseListener {

	public static final int LEFT_MOUSE = 0;
	public static final int MIDDLE_MOUSE = 1;
	public static final int RIGHT_MOUSE = 2;

	private static final boolean[] FALSE_ARRAY = new boolean[3];
	public boolean[] mouseButtonWasPressed = new boolean[3];
	public boolean[] mouseButtonIsPressed = new boolean[3];
	public int x = -1;
	public int y = -1;
	public int prevX;
	public int prevY;

	public boolean confineMouse = true;

	public MouseData() {
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		final int clickedButton = e.getButton();
		if (clickedButton >= 1 && clickedButton <= 3) {
			mouseButtonWasPressed[clickedButton - 1] = true;
			mouseButtonIsPressed[clickedButton - 1] = true;
		}
		updateCoordinates(e);
	}

	private void updateCoordinates(final MouseEvent e) {
		x = e.getX() / EngineCanvas.SCALE;
		y = e.getY() / EngineCanvas.SCALE;
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		final int clickedButton = e.getButton();
		if (clickedButton >= 1 && clickedButton <= 3) {
			mouseButtonIsPressed[clickedButton - 1] = false;
		}
		fixConfinedCursorIfNeeded(e);
		updateCoordinates(e);
	}

	private void fixConfinedCursorIfNeeded(final MouseEvent e) {
		if (confineMouse) {
			try {
				final int margin = 4;
				final Point origo = e.getComponent().getLocationOnScreen();
				final int height = e.getComponent().getHeight();
				final int width = e.getComponent().getWidth();
				final int x = Math.min(origo.x + width - margin,
						Math.max(origo.x + margin, e.getXOnScreen()));
				final int y = Math.min(origo.y + height - margin,
						Math.max(origo.y + margin, e.getYOnScreen()));

				new Robot().mouseMove(x, y);
			} catch (final AWTException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		confineMouseIfNeeded(e);
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		updateCoordinates(e);
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
		confineMouseIfNeeded(e);
		updateCoordinates(e);
	}

	private void confineMouseIfNeeded(final MouseEvent e) {
		if (confineMouse && Arrays.equals(mouseButtonIsPressed, FALSE_ARRAY)) {
			fixConfinedCursorIfNeeded(e);
		}
	}

	public void tick() {
		Arrays.fill(mouseButtonWasPressed, false);
		prevX = x;
		prevY = y;
	}

	public boolean isBeingDragged(final int mouseButton) {
		return mouseButtonIsPressed[mouseButton]
				&& !mouseButtonWasPressed[mouseButton];
	}
}
