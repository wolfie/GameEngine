package com.github.wolfie.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collection;

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

	private final MultiHashMap<Integer, Key> keys = new MultiHashMap<>();

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
		final Collection<Key> keys = this.keys.get(e.getKeyCode());
		for (final Key key : keys) {
			key.nextState = (e.getID() == KeyEvent.KEY_PRESSED);
		}
	}

	public void tick() {
		for (final Key key : keys.values()) {
			key.tick();
		}
	}

	protected void add(final int keycode, final Key... keys) {
		this.keys.putAll(keycode, Arrays.asList(keys));
	}
}
