package com.github.wolfie.fight2;

import java.awt.event.KeyEvent;

public class FightKeyData extends KeyData {

	public final Key jump = new Key();
	public final Key left = new Key();
	public final Key right = new Key();

	public final Key esc = new Key();

	public FightKeyData() {
		add(jump, KeyEvent.VK_W);
		add(left, KeyEvent.VK_A);
		add(right, KeyEvent.VK_D);
		add(esc, KeyEvent.VK_ESCAPE);
	}
}
