package com.github.wolfie.fight2.objects;

import com.github.wolfie.fight2.Screen;
import com.github.wolfie.fight2.TickData;

public class Player extends GameObject implements Colliding {

	private static final int PLAYER_HEIGHT = 16;
	private static final int PLAYER_WIDTH = 16;

	private static final double RUN_SPEED_PPS = 500;
	private static final double RUN_ACCELERATION = 10;
	private static final double RUN_BREAK = 50;

	private static final double JUMP_VELOCITY = 500;
	private boolean hasDoubleJumped = false;

	public Player(final int x, final int y) {
		super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
	}

	@Override
	public void render(final Screen screen) {
		screen.fill(topLeftX, topLeftY, PLAYER_WIDTH, PLAYER_HEIGHT, 0xff000000);
	}

	@Override
	public void _tick(final long msBetweenTicks, final TickData tickData) {
		if (tickData.keys.left.isDown && !tickData.keys.right.isDown) {
			if (velocityXPPS > -RUN_SPEED_PPS) {
				velocityXPPS -= RUN_SPEED_PPS * RUN_ACCELERATION
						* msBetweenTicks / 1000;
			} else {
				velocityXPPS = -RUN_SPEED_PPS;
			}
		}

		else if (tickData.keys.right.isDown && !tickData.keys.left.isDown) {
			if (velocityXPPS < RUN_SPEED_PPS) {
				velocityXPPS += RUN_SPEED_PPS * RUN_ACCELERATION
						* msBetweenTicks / 1000;
			} else {
				velocityXPPS = RUN_SPEED_PPS;
			}
		}

		else {

			if (velocityXPPS != 0) {
				final double deceleration = RUN_SPEED_PPS * RUN_BREAK
						* msBetweenTicks / 1000;
				if (velocityXPPS > 0) {
					velocityXPPS -= deceleration;
					if (velocityXPPS < 0) {
						velocityXPPS = 0;
					}
				} else {
					velocityXPPS += deceleration;
					if (velocityXPPS > 0) {
						velocityXPPS = 0;
					}
				}
			}
		}

		if (tickData.keys.jump.wasPressed() && (isOnGround || !hasDoubleJumped)) {
			if (!isOnGround && !hasDoubleJumped) {
				hasDoubleJumped = true;
			}
			isOnGround = false;
			velocityYPPS = -JUMP_VELOCITY;
		}
	}

	@Override
	public void handleCollision(final GameObject obj) {

	}

	@Override
	public void isOnGround() {
		super.isOnGround();
		hasDoubleJumped = false;
	}
}
