package nl.kozie.twodee;

import nl.kozie.twodee.entity.Tree;
import nl.kozie.twodee.gfx.Spritesheet;

public class Main implements Game {
	
	private static final String TITLE = "Twodee";
	private static final int WIDTH = 320;
	private static final int HEIGHT = 240;
	private static final int SCALE = 3;
	private static final int FPS = 60;
	private static final int UPS = 40;
	
	public Main() {
				
		Display display = new Display(WIDTH, HEIGHT, SCALE);
		display.initWindow();
		display.initMosaic(SCALE);
		display.setTitle(TITLE);
		display.setFps(FPS);
		
		Manager manager = Manager.getInstance(this);
		manager.setDisplay(display);
		manager.setUps(UPS);
		
		try {
			manager.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void init() {
		
		Spritesheet main = new Spritesheet(Manager.getImage("maintiles.png"), 16);
		Manager.setSpritesheet("main", main);
	}
	
	public synchronized void tick(int delta) {
		
		Manager mgr = Manager.getInstance();
		if (mgr.mouse.left) {
			int x = mgr.mouse.mouseX - 8;
			int y = mgr.mouse.mouseY - 8;
			
			Tree ent = new Tree(x, y);
			mgr.sprites.add(ent);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
}