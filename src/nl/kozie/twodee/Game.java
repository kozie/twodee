package nl.kozie.twodee;

import java.awt.Graphics2D;

public interface Game extends Runnable {
	
	public static Object main = null;
	public boolean running = false;
	public Display display = null;
	
	public void start();
	public void stop();
	
	public void tick(int delta);
	public void render(Graphics2D g);
}