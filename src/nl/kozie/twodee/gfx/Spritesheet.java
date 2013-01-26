package nl.kozie.twodee.gfx;

import java.awt.image.BufferedImage;

public class Spritesheet {
	
	protected int[] pixels;
	
	protected int width;
	protected int height;
	
	protected int tileWidth;
	protected int tileHeight;
	
	public Spritesheet(BufferedImage img, int tileSize) {
		this(img, tileSize, tileSize);
	}
	
	public Spritesheet(BufferedImage img, int tw, int th) {
		
		width = img.getWidth();
		height = img.getHeight();
		pixels = img.getRGB(0, 0, width, height, null, 0, width);
		
		for (int i = 0; i < pixels.length; i++) {
			
			int alpha = (pixels[i] >> 24) & 0xFF;
			
			// If alpha is transparent (0) set current pixel to -1.
			if (alpha == 0) pixels[i] = -1;
		}
		
		tileWidth = tw;
		tileHeight = th;
	}
	
	public Sprite getTile(int x, int y) {
		return getTile(x, y, 1, 1);
	}
	
	public Sprite getTile(int x, int y, int tileSize) {
		return getTile(x, y, tileSize, tileSize);
	}
	
	public Sprite getTile(int x, int y, int tsx, int tsy) {
		
		// Check if within image boundaries
		if ((x * tileWidth) + (tsx * tileWidth) > width) return null;
		if ((y * tileHeight) + (tsy * tileHeight) > height) return null;
		
		int offsetX = x * tileWidth;
		int offsetY = y * tileHeight * width;
		
		int spriteW = tsx * tileWidth;
		int spriteH = tsy * tileHeight;
		int[] pix = new int[(tsx * tileWidth) * (tsy * tileHeight)];
		
		for (int yy = 0; yy < (tsy * tileHeight); yy++) {
			for (int xx = 0; xx < (tsx * tileWidth); xx++) {
				pix[(yy * spriteW) + xx] = pixels[offsetY + (yy * width) + offsetX + xx];
			}
		}
		
		return new Sprite(spriteW, spriteH, pix);
	}
}