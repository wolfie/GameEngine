package com.github.wolfie.fight2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public abstract class KeyData implements KeyListener {

	public final class Key {
		public boolean nextState = false;
		public boolean wasDown = false;
		public boolean isDown = false;

		public void tick() {
			wasDown = isDown;
			isDown = nextState;
		}

		public boolean wasPressed() {
			return !wasDown && isDown;
		}

		public boolean wasReleased() {
			return wasDown && !isDown;
		}

		public void release() {
			nextState = false;
		}
	}

	private final Map<Integer, Key> keys = new HashMap<>();

	@Override
	public void keyTyped(final KeyEvent e) {
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		set(e);
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		set(e);
	}

	private void set(final KeyEvent e) {
		final Key key = keys.get(e.getKeyCode());
		if (key != null) {
			key.nextState = (e.getID() == KeyEvent.KEY_PRESSED);
		}
	}

	public void tick() {
		for (final Key key : keys.values()) {
			key.tick();
		}
	}

	protected void add(final Key key, final int keycode) {
		keys.put(keycode, key);
	}
}
