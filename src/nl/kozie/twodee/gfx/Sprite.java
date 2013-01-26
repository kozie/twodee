package nl.kozie.twodee.gfx;

public class Sprite {
	
	protected int width;
	protected int height;
	
	public int[] pixels;
	
	public Sprite(int w, int h, int[] pix) {
		width = w;
		height = h;
		pixels = pix;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}