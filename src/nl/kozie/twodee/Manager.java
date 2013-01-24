package nl.kozie.twodee;

public class Manager implements Runnable {

	protected boolean running = false;
	
	public Display display;
	public Game main;
	public KeyboardListener keyboard;
	
	public static Manager instance;
	public int ups = 40;
	
	protected void init() {
		keyboard = new KeyboardListener(display);
		
		display.initStrategy();
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
	
	public void setDisplay(Display dis) {
		display = dis;
	}
	
	public void setMain(Game obj) {
		main = obj;
	}
	
	public void setUps(int rate) {
		ups = rate;
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
		int toRender = 0;
		
		int tickCount = 0;
		int frameCount = 0;
		long lastCount = System.currentTimeMillis();
		
		while (running) {
			
			if (keyboard.esc.pressed) stop();
			
			while (unprocessedTicks >= 1) {
				toTick++;
				unprocessedTicks -= 1;
			}
			
			if (toTick > 1 && toTick < 3) toTick = 1;
			if (toTick > 20) toTick = 20;
			
			while (toTick > 0) {
				int delta = (int) (System.currentTimeMillis() - lastTime);
				tick(delta);
				
				lastTime = System.currentTimeMillis();
				
				toTick--;
				tickCount++;
			}
			
			while (unprocessedFrames >= 1) {
				toRender++;
				unprocessedFrames -= 1;
			}
			
			if (toRender > 1 && toRender < 3) toRender = 1;
			if (toRender > 20) toRender = 20;
			
			while (toRender > 0) {
				display.render();
				
				toRender--;
				frameCount++;
			}
			
			unprocessedTicks += (System.nanoTime() - lastTick) / nsPerTick;
			lastTick = System.nanoTime();
			
			unprocessedFrames += (System.nanoTime() - lastFrame) / nsPerFrame;
			lastFrame = System.nanoTime();
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Keep a counter for stats
			if ((System.currentTimeMillis() - lastCount) >= 1000) {
				System.out.printf("Tick %d FPS %d\n", tickCount, frameCount);
				
				tickCount = 0;
				frameCount = 0;
				lastCount = System.currentTimeMillis();
			}
		}
		
		System.exit(0);
	}
	
	protected void tick(int delta) {
		System.gc();
	}
}