package nl.kozie.twodee;

import java.util.ArrayList;
import java.util.List;

import nl.kozie.twodee.entity.Tree;
import nl.kozie.twodee.gfx.Spritesheet;
import nl.kozie.twodee.world.World;

public class Main implements Game {
	
	private static final String TITLE = "Twodee";
	private static final int WIDTH = 320;
	private static final int HEIGHT = 240;
	private static final int SCALE = 3;
	private static final int FPS = 60;
	private static final int UPS = 40;
	
	protected List<String> coords = new ArrayList<String>();
	
	public Main() {
				
		Display display = new Display(WIDTH, HEIGHT, SCALE);
		display.initWindow();
		display.initMosaic(SCALE, 0.12, 0xB2D5FF, 0x57007F);
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
		
		Spritesheet main = new Spritesheet(Resources.getImage("maintiles.png"), 16);
		Resources.setSpritesheet("main", main);
		
		World w = new World();
		w.init(300, 300);
		w.setBaseTile(main.getTile(1, 1));
		Manager.getInstance().setWorld(w);
	}
	
	public synchronized void draw() {
		Manager.getInstance().getWorld().draw();
	}
	
	public synchronized void tick(int delta) {
		
		Manager mgr = Manager.getInstance();
		KeyboardListener keyboard = mgr.keyboard;
		MouseListener mouse = mgr.mouse;
		
		if (mouse.left) {
			int x = mouse.mouseX - 8;
			int y = mouse.mouseY - 8;
			String xy = x + "," + y;
			
			if (!coords.contains(xy)) {
			
				Tree ent = new Tree(x, y);
				mgr.sprites.add(ent);
			
				coords.add(xy);
			}
		}
		
		if (keyboard.left.pressed) mgr.getWorld().scrollLeft(1);
		if (keyboard.up.pressed) mgr.getWorld().scrollUp(1);
		if (keyboard.down.pressed) mgr.getWorld().scrollDown(1);
		if (keyboard.right.pressed) mgr.getWorld().scrollRight(1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
}