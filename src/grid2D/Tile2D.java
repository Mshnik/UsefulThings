package grid2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import grid.Grid;
import grid.Location;
import grid.Tile;

public class Tile2D extends Tile implements Shape{

	private static final String[] LABELS = {"row", "col"};
	
	public static final int ROW_INDEX = 0;
	public static final int COL_INDEX = 1;
	
	public final int row;
	public final int col;
	
	/** The x graphic location of this tile (top left corner). Should only be used for graphic functionality */
	double x;
	
	/** The y graphic location of this tile (top left corner). Should only be used for graphic functionality */
	double y;
	
	/** The graphic width of this tile */
	double width;
	
	/** The graphic height of this tile */
	double height;
	
	 
	/***/
	public Tile2D(Grid<Tile2D> g, int row, int col){
		super(g, Location.initBuilder(g.getMinVals(), g.getMaxVals(), LABELS).build(row, col));
		this.row = row;
		this.col = col;
	}	
	
	/** Draws this Tile2D graphically */
	public void draw(Graphics2D g2d){
		if(selected)
			g2d.setColor(Color.GREEN);
		else if( (row + col) % 2 == 0)
			g2d.setColor(Color.black);
		else
			g2d.setColor(Color.white);
		
		g2d.fillRect((int)x, (int)y, (int)width, (int)height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	@Override
	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(x, y, width, height);
	}

	@Override
	public boolean contains(double x, double y) {
		return x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height;
	}

	@Override
	public boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return intersects(new Rectangle2D.Double(x, y, w, h));
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return getBounds2D().intersects(r);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return getBounds2D().contains(x, y, w, h);
	}

	@Override
	public boolean contains(Rectangle2D r) {
		return getBounds2D().contains(r);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return getBounds2D().getPathIterator(at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return getBounds2D().getPathIterator(at, flatness);
	}

}
