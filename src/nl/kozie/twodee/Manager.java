package nl.kozie.twodee;

import java.util.List;
import java.util.Vector;

import nl.kozie.twodee.entity.Tree;
import nl.kozie.twodee.gfx.Screen;
import nl.kozie.twodee.world.World;

public class Manager implements Runnable {
	
	protected boolean running = false;
	public int ups = 40;
	
	public Display display;
	public Screen screen;
	public KeyboardListener keyboard;
	public MouseListener mouse;
	
	public static Manager instance;
	public static Game main;
	
	public World world;
	
	// TODO Temp for fun
	public List<Tree> sprites = new Vector<Tree>();
	
	protected Manager() {}
	
	protected void init() {
		
		keyboard = new KeyboardListener(display);
		mouse = new MouseListener(display);
		
		Resources.getInstance();
		
		main.init();
	}
	
	public static Manager getInstance() {
		if (instance == null) {
			instance = new Manager();
		}
		
		return instance;
	}
	
	public static Manager getInstance(Game obj) {
		Manager manager = getInstance();
		manager.setMain(obj);
		
		return manager;
	}
	
	public static Game getMain() {
		return main;
	}
	
	public void setMain(Game obj) {
		main = obj;
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public void setDisplay(Display dis) {
		display = dis;
	}
	
	public void setUps(int rate) {
		ups = rate;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World w) {
		world = w;
	}
	
	public void start() throws Exception {
		
		running = true;
		
		if (display == null) {
			throw new Exception("Manager needs a display");
		}
		
		if (main == null) {
			throw new Exception("Manager needs a main object");
		}
		
		Thread thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	
	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		
		init();
		
		double nsPerTick = 1000000000.0 / ups;
		double nsPerFrame = 1000000000.0 / display.fps;
		double unprocessedTicks = 0;
		double unprocessedFrames = 0;
		
		long lastTick = System.nanoTime();
		long lastFrame = System.nanoTime();
		long lastTime = System.currentTimeMillis();
		
		int toTick = 0;
		int gonnaTick = 0;
		int toRender = 0;
		int gonnaRender = 0;
		
		int tickCount = 0;
		int frameCount = 0;
		long lastCount = System.currentTimeMillis();
		
		while (running) {
			
			if (keyboard.esc.pressed) stop();
			
			while (unprocessedTicks >= 1) {
				toTick++;
				unprocessedTicks -= 1;
			}
			
			while (unprocessedFrames >= 1) {
				toRender++;
				unprocessedFrames -= 1;
			}
			
			gonnaTick = toTick;
			if (toTick > 0 && toTick < 3) gonnaTick = 1;
			if (toTick > 20) toTick = 20;
			
			for (int i = 0; i < gonnaTick; i++) {
				int delta = (int) (System.currentTimeMillis() - lastTime);
				main.tick(delta);
				
				lastTime = System.currentTimeMillis();
				
				toTick--;
				tickCount++;
			}
			
			gonnaRender = toRender;
			if (toRender > 0 && toRender < 3) gonnaRender = 1;
			if (toRender > 20) toRender = 20;
			
			for (int i = 0; i < gonnaRender; i++) {
				main.draw();
				display.render();
				
				toRender--;
				frameCount++;
			}
			
			long now = System.nanoTime();
			unprocessedTicks += (now - lastTick) / nsPerTick;
			unprocessedFrames += (now - lastFrame) / nsPerFrame;
			lastTick = now;
			lastFrame = now;
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Keep a counter for stats
			long currentTime = System.currentTimeMillis();
			if ((currentTime - lastCount) >= 1000) {
				System.out.printf("Tick %d FPS %d\n", tickCount, frameCount);
				
				tickCount = 0;
				frameCount = 0;
				lastCount = currentTime;
			}
		}
		
		System.exit(0);
	}
}