package nl.kozie.twodee;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import nl.kozie.twodee.gfx.Spritesheet;

public class Manager implements Runnable {

	public static final byte RESOURCE_TYPE_IMAGE = 0x01;
	public static final byte RESOURCE_TYPE_FONT = 0x02;
	public static final byte RESOURCE_TYPE_SFX = 0x04;
	public static final byte RESOURCE_TYPE_MUSIC = 0x08;
	
	protected boolean running = false;
	public int ups = 40;
	
	public Display display;
	public KeyboardListener keyboard;
	
	public static Manager instance;
	public static Game main;
	
	public static Map<String, Spritesheet> spritesheets;
	
	protected void init() {
		
		keyboard = new KeyboardListener(display);
		spritesheets = Collections.synchronizedMap(new HashMap<String, Spritesheet>());
		
		display.initStrategy();
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
	
	public static Spritesheet getSpritesheet(String key) {
		if (spritesheets.containsKey(key)) {
			return spritesheets.get(key);
		}
		
		return null;
	}
	
	public static void setSpritesheet(String key, Spritesheet sheet) {
		spritesheets.put(key, sheet);
	}
	
	public static BufferedImage getImage(String image) {
		
		InputStream resource = getResource(RESOURCE_TYPE_IMAGE, image);
		
		if (resource != null) {
			return getImage(resource);
		}
		
		return null;
	}
	
	public static BufferedImage getImage(InputStream image) {
		
		try {
			BufferedImage img = ImageIO.read(image);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static InputStream getSfx(String sfx) {
		return getResource(RESOURCE_TYPE_SFX, sfx);
	}
	
	public static InputStream getMusic(String music) {
		return getResource(RESOURCE_TYPE_MUSIC, music);
	}
	
	public static InputStream getResource(byte type, String file) {
		
		String folder = "";
		switch (type) {
		
			case RESOURCE_TYPE_IMAGE:
				folder = "/images/";
				break;
				
			case RESOURCE_TYPE_FONT:
				folder = "/fonts/";
				break;
				
			case RESOURCE_TYPE_SFX:
				folder = "sfx/";
				break;
				
			case RESOURCE_TYPE_MUSIC:
				folder = "music/";
				break;
		}
		
		return Manager.class.getResourceAsStream(folder + file);
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
};