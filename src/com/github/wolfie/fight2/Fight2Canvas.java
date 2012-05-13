package com.github.wolfie.fight2;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Fight2Canvas extends Canvas implements Runnable {

	public class FightFocusListener implements FocusListener {

		@Override
		public void focusGained(final FocusEvent e) {
		}

		@Override
		public void focusLost(final FocusEvent e) {
			pauseGame();
		}

	}

	public class FightWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(final WindowEvent e) {
			running = false;
		}
	}

	private static final String GAME_TITLE = "Fight 2";

	private static final int SCALE = 2;
	public static final int FIGHT_WIDTH = 640;
	public static final int FIGHT_HEIGHT = 360;

	private static final int TICK_LENGHT_MILLIS = 20;
	private static final int FPS_CAP = 1000 / TICK_LENGHT_MILLIS;
	private static final int MIN_MILLIS_PER_FRAME = TICK_LENGHT_MILLIS;

	/** gravity, pixels per second, per second */
	public static final double GRAVITY_PPSS = 1000;

	public static Fight2Canvas instance;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// System.setProperty("sun.java2d.opengl", "True");
		// System.setProperty("sun.java2d.d3d", "True");
		// System.setProperty("sun.java2d.noddraw", "True");
		final JFrame frame = new JFrame(GAME_TITLE);
		final JPanel panel = new JPanel(new BorderLayout());
		final Fight2Canvas game = new Fight2Canvas();
		panel.add(game);
		frame.setContentPane(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(game.new FightWindowListener());
		frame.setVisible(true);
		game.start();
	}

	private boolean running;

	public final FightKeyData keyData = new FightKeyData();
	private final MouseData mouseData = new MouseData();
	private final Screen screen = new Screen(FIGHT_WIDTH, FIGHT_HEIGHT);
	private final PauseScreen pauseScreen = new PauseScreen();
	public final Stack<GuiElement> guiStack = new Stack<>();

	public Fight2Canvas() {
		instance = this;

		setPreferredSize(getGameDimensions());
		setMinimumSize(getGameDimensions());
		setMaximumSize(getGameDimensions());

		addMouseMotionListener(mouseData);
		addMouseListener(mouseData);

		guiStack.add(new GameScreen());
	}

	private static Dimension getGameDimensions() {
		return new Dimension(getScaledWidth(), getScaledHeight());
	}

	public static int getScaledWidth() {
		return FIGHT_WIDTH * SCALE;
	}

	public static int getScaledHeight() {
		return FIGHT_HEIGHT * SCALE;
	}

	private void start() {
		running = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		init();

		long currentTime = System.currentTimeMillis();
		long nextTick = currentTime + TICK_LENGHT_MILLIS;
		long nextFrame = currentTime + MIN_MILLIS_PER_FRAME;
		long lastTick = currentTime;

		while (running) {
			currentTime = System.currentTimeMillis();
			if (nextTick <= currentTime) {
				tick(currentTime - lastTick);

				while (nextTick <= currentTime) {
					nextTick += TICK_LENGHT_MILLIS;
					if (nextTick <= currentTime) {
						System.err.println("tick was missed!");
					}
				}
				lastTick = currentTime;
			}

			final BufferStrategy bs = getEnsuredBufferStrategy();
			if (nextFrame <= currentTime) {
				render(bs.getDrawGraphics());
				nextFrame = currentTime + MIN_MILLIS_PER_FRAME;
				bs.show();
			}

			try {
				Thread.sleep(getSleepAmountMillis(currentTime, nextTick,
						nextFrame));
			} catch (final InterruptedException e) {
				e.printStackTrace();
				break;
			}

			lastTick = currentTime;
		}

		System.out.println("Closing game. Have a nice day!");
		System.exit(0);
	}

	private BufferStrategy getEnsuredBufferStrategy() {

		BufferStrategy bufferStrategy = null;
		while (bufferStrategy == null) {
			bufferStrategy = getBufferStrategy();
			if (bufferStrategy == null) {
				System.out.println("recreating buffer strategy");
				createBufferStrategy(3);
			}
		}
		return bufferStrategy;
	}

	@Override
	public void paint(final Graphics g) {
		// disable default implementation
	}

	@Override
	public void update(final Graphics g) {
		// disable default implementation
	}

	private void tick(final long msSinceLastTick) {
		keyData.tick();
		final TickData tickData = new TickData(keyData);
		if (!pauseScreen.isPaused()) {
			guiStack.peek().tick(msSinceLastTick, tickData);
		} else {
			pauseScreen.tick(msSinceLastTick, tickData);
		}
	}

	private void render(final Graphics g) {
		guiStack.peek().render(screen);
		g.setColor(Color.WHITE);

		g.fillRect(0, 0, getWidth(), getHeight());
		g.translate((getWidth() - getScaledWidth()) / 2,
				(getHeight() - getScaledHeight()) / 2);
		g.clipRect(0, 0, getScaledWidth(), getScaledHeight());

		final JLabel label = new JLabel("hello");
		label.setFont(new Font("Sans Serif", Font.PLAIN, 16));
		label.setForeground(Color.BLACK);
		label.paint(g);

		final BufferedImage image = toCompatibleImage(screen.getImage());
		g.drawImage(image, 0, 0, getScaledWidth(), getScaledHeight(), null);
	}

	private BufferedImage toCompatibleImage(final BufferedImage image) {
		// obtain the current system graphical settings
		final GraphicsConfiguration gfx_config = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system
		 * settings, simply return it
		 */
		if (image.getColorModel().equals(gfx_config.getColorModel())) {
			return image;
		}

		// image is not optimized, so create a new image that is
		final BufferedImage new_image = gfx_config.createCompatibleImage(
				image.getWidth(), image.getHeight(), image.getTransparency());

		// get the graphics context of the new image to draw the old image on
		final Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		// return the new optimized image
		return new_image;
	}

	private static long getSleepAmountMillis(final long currentTime,
			final long nextTick, final long nextFrame) {
		final long millisUntilTick = nextTick - currentTime;
		final long millisUntilFrame = nextFrame - currentTime;
		return Math.max(0, Math.min(millisUntilTick, millisUntilFrame));
	}

	private void init() {
		addKeyListener(keyData);
		setFocusTraversalKeysEnabled(false);
		requestFocus();
		addFocusListener(new FightFocusListener());
	}

	public void pauseGame() {
		if (!pauseScreen.isPaused()) {
			pauseScreen.pause();
			guiStack.add(pauseScreen);
		}
	}

	public void unpauseGame() {
		if (pauseScreen.isPaused()) {
			pauseScreen.unpause();
			GuiElement element = null;
			while (element != pauseScreen) {
				element = guiStack.pop();
			}
		}
	}
}
