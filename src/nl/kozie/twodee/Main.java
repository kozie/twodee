package nl.kozie.twodee;

import java.awt.Dimension;
import java.awt.Graphics2D;

public class Main implements Game {
	
	private static final String TITLE = "Twodee";
	private static final int WIDTH = 320;
	private static final int HEIGHT = 240;
	private static final int SCALE = 3;
	private static final int FRAMERATE = 60;
	
	public static Main instance;
	public boolean running = false;
	
	public Display display;
	public KeyboardListener keyboard;
	
	public Main() {
		
		instance = this;
		
		Dimension dim = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		
		display = new Display(dim);
		display.setTitle(TITLE);
		display.setFps(FRAMERATE);
		display.show();
		
		keyboard = new KeyboardListener(display);
		
		this.start();
	}
	
	public void start() {
		running = true;
		
		Thread thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	
	public void stop() {
		running = false;
	}
	
	public void run() {
		
	}
	
	public void tick(int delta) {
		
	}
	
	public void render(Graphics2D g) {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
}