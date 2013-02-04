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

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.kozie.twodee.gfx.Sprite;

public class Display extends Canvas {
	
	private static final long serialVersionUID = 1L;
	public static final int BIT_MIRROR_X = 0x01;
	public static final int BIT_MIRROR_Y = 0x02;
	public static final int BIT_MIRROR_BOTH = 0x03;
	
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
		
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
		
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
		
		mosaicImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		//mosaicImage = gfxConfig.createCompatibleImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);
		mosaicPixels = ((DataBufferInt) mosaicImage.getRaster().getDataBuffer()).getData();
		
		for (int y = 0; y < getHeight(); y ++) {
			for (int x = 0; x < getWidth(); x++) {
				
				int col = spr.getPixel(x % scale, y % scale);
				int idx = y * getWidth() + x;
				
				if (col != -1) {
					mosaicPixels[idx] = (mosaicAlpha << 24) | col;
				}
			}
		}
	}
	
	public void draw(int[] pixels, int w, int h) {
		draw(pixels, w, h, 0, 0);
	}
	
	public void draw(int[] pixels, int w, int h, int x, int y) {
		draw(pixels, w, h, x, y, 0);
	}
	
	public void draw(int[] pixels, int w, int h, int x, int y, int bit) {
		
		boolean mirrorX = (bit & BIT_MIRROR_X) > 0;
		boolean mirrorY = (bit & BIT_MIRROR_Y) > 0;
		
		int offset = y * width + x;
		
		for (int yy = 0; yy < h; yy++) {
			for (int xx = 0; xx < w; xx++) {
				
				if (xx + x > (width - 1) || xx + x < 0 || yy + y > (height - 1) || yy + y < 0) continue;
				
				int xi = xx;
				int yi = yy;
				
				if (mirrorX) xi = (w - 1) - xx;
				if (mirrorY) yi = (h - 1) - yy;
				
				int col = pixels[yi * h + xi];
				
				if (col != -1) {
					this.pixels[offset + (yy * width) + xx] = col;
				}
			}
		}
	}
	
	public void render() {
		
		initBufferStrategy();
		
		Manager mgr =  Manager.getInstance();
		for (int i = 0; i < mgr.sprites.size(); i++) {
			mgr.sprites.get(i).draw();
		}
		
		g.setColor(Color.BLACK);
		g.clearRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		// Blend with mosaic?
		if (mosaicImage != null) {
			g.drawImage(mosaicImage, 0, 0, getWidth(), getHeight(), null);
		}
				
		sync();
		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		clear();
	}
	
	public void sync() {
		g.dispose();
		bs.show();
		Toolkit.getDefaultToolkit().sync();
		
		// TODO Fix disposing of bs without flickering
		//bs.dispose();
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
}