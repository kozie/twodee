package nl.kozie.twodee;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
}