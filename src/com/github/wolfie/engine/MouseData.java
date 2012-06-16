package com.github.wolfie.engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

public class MouseData implements MouseMotionListener, MouseListener {

	public boolean[] mouseButtonWasPressed = new boolean[3];
	public boolean[] mouseButtonIsPressed = new boolean[3];

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
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		final int clickedButton = e.getButton();
		if (clickedButton >= 1 && clickedButton <= 3) {
			mouseButtonIsPressed[clickedButton - 1] = false;
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
	}

	public void postTick() {
		Arrays.fill(mouseButtonWasPressed, false);
	}
}
