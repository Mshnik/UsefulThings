package matching;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private HashSet<SpacialMatchObject> objects;
	private HashMap<MatchObject.Type, Color> color;
	private Panel panel;
	private static final int UPDATE_TIME = 100;
	
	public GUI(HashSet<SpacialMatchObject> objects){
		if(objects != null)
			this.objects = new HashSet<SpacialMatchObject>(objects);
		else
			this.objects = new HashSet<>();
		color = new HashMap<>();
		color.put(MatchObject.Type.A, Color.red);
		color.put(MatchObject.Type.B, Color.blue);
		panel = new Panel();
		add(panel, BorderLayout.CENTER);
		pack();
		repaint();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(UPDATE_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					iterate();
				}
			}
			
		}).start();
	}
	
	
	
	private void iterate(){
		SpacialMatchObject.iterate(objects);
		repaint();
	}
	
	class Panel extends JPanel{
		
		private static final int DIAMETER = 10;
		
		public Panel(){
			setPreferredSize(new Dimension(800, 800));
		}
		
		@Override
		public void paintComponent(Graphics g){
			Graphics2D g2d = (Graphics2D) g;
			
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			
			final double xMin = SpacialMatchObject.getMinX() - DIAMETER * 2;
			final double yMin = SpacialMatchObject.getMinY() - DIAMETER * 2;
			final double width = SpacialMatchObject.getMaxX() - xMin + DIAMETER * 2;
			final double height = SpacialMatchObject.getMaxY() - yMin + DIAMETER * 2;
			
			for(SpacialMatchObject smo : objects){
				g2d.setColor(color.get(smo.type));
				Point2D loc = smo.getLocation();
				Point2D scaledLoc = new Point2D.Double(
						(loc.getX() - xMin) / width * (getWidth()), 
						(loc.getY() - yMin) / height * (getHeight())
					);
				
				g2d.fillOval((int) scaledLoc.getX(), (int) scaledLoc.getY(), DIAMETER, DIAMETER);
				g2d.setColor(Color.BLACK);
				g2d.drawString(smo.id + "", (int) scaledLoc.getX() + DIAMETER/2, 
											(int) scaledLoc.getY() + DIAMETER/2);
			}
		}
	}
	
}
