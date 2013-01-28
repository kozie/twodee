package nl.kozie.twodee.gfx;

import nl.kozie.twodee.Display;
import nl.kozie.twodee.Manager;

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
	
	public void render() {
		render(0, 0);
	}
	
	public void render(int x, int y) {
			render(x, y, 0);
	}
	
	public void render(int x, int y, int bit) {
		
		Display display = Manager.getInstance().getDisplay();
		display.draw(pixels, width, height, x, y, bit);
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