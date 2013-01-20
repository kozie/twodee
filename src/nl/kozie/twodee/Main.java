package nl.kozie.twodee;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends Canvas implements Runnable {
	
	private static final String TITLE = "Twodee";
	private static final int WIDTH = 320;
	private static final int HEIGHT = 240;
	private static final int SCALE = 3;
	private static final int FRAMERATE = 60;
	
	public static Main main;
	public KeyboardListener keyboard;
	private boolean running = false;
	
	public Main() {
		
		main = this;
		
		Dimension dim = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		
		keyboard = new KeyboardListener(this);
	}
	
	public void init() {
		requestFocus();
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

	@Override
	public void run() {
		
		// Initialize first
		init();
		
		// Set rates and variables to keep track
		double nsPerTick = 1000000000.0 / FRAMERATE;
		
		while (running) {
			
			// Exit on escape
			if (keyboard.esc.pressed) stop();
		}
		
		// Exit when here
		System.exit(0);
	}
	
	public void tick(int delta) {
		
	}
	
	public void render(Graphics2D g) {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Main main = new Main();
		JFrame frame = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(main);
		frame.setContentPane(panel);
		frame.setUndecorated(true);
		frame.pack();
		frame.setTitle(TITLE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		main.start();
	}
}