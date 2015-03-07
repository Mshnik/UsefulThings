package grid.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import grid.Grid;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Board extends JPanel {

	/***/
	private static final long serialVersionUID = 1L;
	
	/** Margin between tiles and end of panel in x direction */
	private int xMargin;
	
	/** Margin between tiles and end of panel in y direction */
	private int yMargin;
	
	/** Margin between tiles in x direction */
	private int xSpace;
	
	/** Margin between tiles in y direction */
	private int ySpace;
	
	/** The grid this Board is responsible for drawing */
	private Grid<? extends DrawableTile> grid;
	
	/** The background color to draw behind the tiles drawn on this board */
	private Color background;
	
	public Board(Grid<? extends DrawableTile> grid, int xMargin, int yMargin, 
			int xSpace, int ySpace, Color background){
		this.grid = grid;
		this.xMargin = xMargin;
		this.yMargin = yMargin;
		this.xSpace = xSpace;
		this.ySpace = ySpace;
		this.background = background;
		
		int width = grid.getBounds()[1];
		int height = grid.getBounds()[0];
		
		setPreferredSize(new Dimension(width * 50, height * 50));
		
		//Register a resize listener to make sure tile graphic attributes are kept up to date
		addComponentListener(new ComponentListener(){
			@Override
			public void componentResized(ComponentEvent e) {
				fixGraphicAttributes();
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
	}
	
	/** Fixes the graphic qualities of the grid this is displaying whenever this is resized */
	private void fixGraphicAttributes(){
		
		int width = grid.getBounds()[0];
		int height = grid.getBounds()[1];
		
		int tileWidth = (getWidth() - xMargin * 2) / width;
		int tileHeight = (getHeight() - yMargin * 2) / height;
		
		for(DrawableTile t : grid){
			int col = t.col;
			int row = t.row;
			t.x = col * tileWidth + xSpace/2 + xMargin;
			t.y = row * tileHeight + ySpace/2 + yMargin;
			t.width = tileWidth - xSpace;
			t.height = tileHeight - ySpace;
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		if(background != null){
			g2d.setColor(background);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
		
		for(DrawableTile t : grid){
			t.draw(g2d);
		}
	}
	
	public static void main(String[] args){
		JFrame f = new JFrame();
		
		
		Grid<DrawableTile> g = new Grid<DrawableTile>(5, 5);
		for(Integer[] i : g.buildCoordinates()){
			//g.add(new DrawableTile(g, i[0], i[1]));
		}
		
		f.add(new Board(g, 50, 50, 5, 5, Color.RED));
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
