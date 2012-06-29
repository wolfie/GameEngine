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

	private static final int MARGIN_Y = 5;
	private static final int MARGIN_X = 5;

	private static final boolean[] FALSE_ARRAY = new boolean[3];
	public boolean[] mouseButtonWasPressed = new boolean[3];
	public boolean[] mouseButtonIsPressed = new boolean[3];
	public boolean[] mouseButtonWasDragged = new boolean[3];
	public boolean[] mouseButtonIsDragged = new boolean[3];
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
			mouseButtonIsPressed[clickedButton - 1] = true;
		}
		updateCoordinates(e);
	}

	private void updateCoordinates(final MouseEvent e) {
		x = (e.getX() - MARGIN_X) / EngineCanvas.SCALE;
		y = (e.getY() - MARGIN_Y) / EngineCanvas.SCALE;
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		final int clickedButton = e.getButton();
		if (clickedButton >= 1 && clickedButton <= 3) {
			mouseButtonIsPressed[clickedButton - 1] = false;
			mouseButtonIsDragged[clickedButton - 1] = false;
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
		for (int i = 0; i < mouseButtonIsPressed.length; i++) {
			mouseButtonIsDragged[i] = mouseButtonIsPressed[i];
		}
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
		System.arraycopy(mouseButtonIsPressed, 0, mouseButtonWasPressed, 0,
				mouseButtonIsPressed.length);
		System.arraycopy(mouseButtonIsDragged, 0, mouseButtonWasDragged, 0,
				mouseButtonIsDragged.length);
		prevX = x;
		prevY = y;
	}
}
