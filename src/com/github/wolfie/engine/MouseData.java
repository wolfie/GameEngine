package com.github.wolfie.engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

public class MouseData implements MouseMotionListener, MouseListener {

	public static final int LEFT_MOUSE = 0;
	public static final int MIDDLE_MOUSE = 1;
	public static final int RIGHT_MOUSE = 2;

	public boolean[] mouseButtonWasPressed = new boolean[3];
	public boolean[] mouseButtonIsPressed = new boolean[3];
	public int x = -1;
	public int y = -1;
	public int prevX;
	public int prevY;

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
		updateCoordinates(e);
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		updateCoordinates(e);
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
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
