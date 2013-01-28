package nl.kozie.twodee.gfx;

public class Screen {

	public int width;
	public int height;
	
	public int[] pixels;
	
	public Screen(int w, int h) {
		width = w;
		height = h;
		pixels = new int[width * height];
	}
}