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
	
	public int getPixel(int x, int y) {
		return pixels[(y * width) + x];
	}
	
	public void setPixel(int col, int x, int y) {
		pixels[(y * width) + x] = col;
	}
	
	public void clear() {
		clear(0);
	}
	
	public void clear(int col) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = col;
		}
	}
}