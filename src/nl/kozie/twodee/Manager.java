package nl.kozie.twodee;

public class Manager implements Runnable {

	public boolean running = false;
	
	public Display display;
	public Game main;
	public KeyboardListener keyboard;
	
	public static Manager instance;
	public int ups = 40;
	
	protected void init() {
		keyboard = new KeyboardListener(display);
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
		
		while (running) {
			
			if (keyboard.esc.pressed) stop();
			
			System.out.println("Im running!");
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.exit(0);
	}
}