package nl.kozie.twodee;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.kozie.twodee.gfx.Sprite;
import nl.kozie.twodee.gfx.Spritesheet;

public class Display extends Canvas {
	
	private static final long serialVersionUID = 1L;
	protected JFrame frame;
	
	protected GraphicsConfiguration gfxConfig;
	protected BufferStrategy bs;
	protected Graphics2D g;
	protected BufferedImage image;
	public int[] pixels;
	
	protected BufferedImage mosaicImage;
	protected int[] mosaicPixels;
	
	public int fps = 60;
	public int width;
	public int height;
	public int scale;
	
	public Display(int w, int h, int s) {
		
		width = w;
		height = h;
		scale = s;
		
		Dimension dim = new Dimension(width * scale, height * scale);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		
		gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDefaultConfiguration();
		
		//image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image = gfxConfig.createCompatibleImage(width, height, Transparency.OPAQUE);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
	public Display(int w, int h) {
		this(w, h, 1);
	}
	
	public void setTitle(String title) {
		if (frame != null) {
			frame.setTitle(title);
		}
	}
	
	public void setFps(int rate) {
		fps = rate;
	}
	
	public void initWindow() {
		frame = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(this);
		frame.setContentPane(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		requestFocus();
	}
	
	public void initBufferStrategy() {
		if (getBufferStrategy() == null) {
			createBufferStrategy(3);
		}
		
		bs = getBufferStrategy();
		g = (Graphics2D) bs.getDrawGraphics();
	}
	
	public void initMosaic(int scale) {
		initMosaic(scale, 0.12);
	}
	
	public void initMosaic(int scale, double alpha) {
		initMosaic(scale, alpha, 0xE0E0E0, 0X00);
	}
	
	public void initMosaic(int scale, double alpha, int light, int dark) {
		
		int mosaicAlpha = ((int) (alpha * 0xFF) & 0xFF);
		
		light = light & 0xFFFFFF;
		dark = dark & 0xFFFFFF;
		
		int[] pix = new int[scale * scale];
		Sprite spr = new Sprite(scale, scale, pix);
		spr.clear(-1);
		
		spr.setPixel(light, 0, 0);
		spr.setPixel(dark, scale - 1, scale - 1);
		
		for (int x = 1; x <= (scale - 1); x++) {
			spr.setPixel(light, x, 0);
			spr.setPixel(dark, x, scale - 1);
		}
		
		for (int y = 1; y <= (scale - 1); y++) {
			spr.setPixel(light, 0, y);
			spr.setPixel(dark, scale - 1, y);
		}
		
		//mosaicImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		mosaicImage = gfxConfig.createCompatibleImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);
		mosaicPixels = ((DataBufferInt) mosaicImage.getRaster().getDataBuffer()).getData();
		
		for (int y = 0; y < getHeight(); y ++) {
			for (int x = 0; x < getWidth(); x++) {
				
				int col = spr.getPixel(x % scale, y % scale);
				int idx = y * getWidth() + x;
				
				if (col == -1) {
					mosaicPixels[idx] = 0x0;
				} else {
					mosaicPixels[idx] = (mosaicAlpha << 24) | col;
				}
			}
		}
	}
	
	public void render() {
		
		initBufferStrategy();
		
		Random rand = new Random();
		for (int i = 0; i < pixels.length; i++) {
			int r = rand.nextInt(0x77) & 0xFF;
			int rgb = (r << 16 | r << 8 | r);
			
			pixels[i] = rgb;
		}
		
		Spritesheet sheet = Manager.getSpritesheet("main");
		Sprite sprite = sheet.getTile(0, 9, 3);
		
		int startX = 140;
		int startY = 80 * width;
		for (int y = 0; y < sprite.getHeight(); y ++) {
			for (int x = 0; x < sprite.getWidth(); x++) {
				int rgb = sprite.pixels[y * sprite.getWidth() + x];
				
				if (rgb == -1) continue;
				
				int r = ((rgb >> 16) & 0xFF);
				int g = ((rgb >> 8) & 0xFF);
				int b = (rgb & 0xFF);
				
				pixels[startY + (y * width) + startX + x] = (r << 16 | g << 8 | b);
			}
		}
		
		g.setColor(Color.RED);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		// Blend with mosaic?
		if (mosaicImage != null) {
			g.drawImage(mosaicImage, 0, 0, getWidth(), getHeight(), null);
		}
				
		sync();
	}
	
	public void sync() {
		g.dispose();
		bs.show();
		Toolkit.getDefaultToolkit().sync();
	}
}