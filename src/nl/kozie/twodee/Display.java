package nl.kozie.twodee;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends Canvas {
	
	private static final long serialVersionUID = 1L;
	protected JFrame frame;
	
	public BufferedImage image;
	public int[] pixels;
	
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
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
	public void setTitle(String title) {
		if (frame != null) {
			frame.setTitle(title);
		}
	}
	
	public void setFps(int rate) {
		fps = rate;
	}
	
	public void show() {
		frame = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(this);
		frame.setContentPane(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void render(Graphics2D g) {
		
	}
}