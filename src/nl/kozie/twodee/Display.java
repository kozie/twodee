package nl.kozie.twodee;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends Canvas {
	
	private static final long serialVersionUID = 1L;
	protected JFrame frame;
	
	public int fps = 60;
	
	public Display(Dimension dim) {
		
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		
		frame = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(this);
		frame.setContentPane(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	
	public void setFps(int fps) {
		this.fps = fps;
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	public void render(Graphics2D g) {
		
	}
}