package graph;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.*;

public class GraphFrame<V,E> extends JFrame {

	private static final long serialVersionUID = 1L;

	private HashMap<V, Circle> nodes;
	private HashMap<E, Line> edges;

	private Dimension size = new Dimension(500,500);

	private GraphFrame(Graph<V,E> graph){
		nodes = new HashMap<>();
		edges = new HashMap<>();

		setLayout(null);
		setSize(size);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		int i = 0;
		for(V v : graph.vertexSet()){
			nodes.put(v, new Circle(v, 100 + (i++)*50, (int)(Math.random() *100)+50, Circle.DEFAULT_DIAMETER));
			getContentPane().add(nodes.get(v));
		}

		for(E e : graph.edgeSet()){
			Graph<V,E>.Edge edge = graph.getEdge(e);
			Circle c1 = nodes.get(edge._1.v);
			Circle c2 = nodes.get(edge._3.v);
			Line l = new Line(c1, c2, graph.getEdge(e));
			edges.put(e, l);
			c1.lines.add(l);
			c2.lines.add(l);
			getContentPane().add(edges.get(e));
		}
	}
	
	public static void main(String[] args){
		Graph<String, Integer> g = new Graph<>();
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addVertex("D");
		g.addEdge("A","C",1);
		g.addEdge("B","C",2);
		g.addEdge("C","D",3);
		
		new GraphFrame<String, Integer>(g);		
	}
	
	public static <V,E> void showGraph(Graph<V,E> g){
		new GraphFrame<V,E>(g);
	}

	/** Graphics class Circle  allows the drawing of circles.
	 * Circles use an (x1, y1, diameter) coordinate system of where they are located
	 * on the board.
	 * Each Circle is tied to and represents a Vertex in the graph.
	 * @author MPatashnik
	 */
	class Circle extends JPanel {

		private static final long serialVersionUID = 1250263410666963976L;

		/** The default diameter of Circles in pixels when drawn on the GUI */ 
		public static final int DEFAULT_DIAMETER = 25;

		/** Extra space to add on diameter when calculating bounds*/
		public static final int PANEL_BUFFER = DEFAULT_DIAMETER/2;

		/** The minimum amount of distance between Circles in pixels when drawn on the GUI */
		public static final int BUFFER_RADUIS = DEFAULT_DIAMETER * 5;

		protected V represents;

		private int x1;
		private int y1;
		private int diameter;

		private Color color;
		private boolean filled;
		
		private HashSet<Line> lines; //Edges attached to this circle

		private Point clickPoint; //The point the user clicked within the circle before dragging began
		private int maxX;   //Boundary for dragging on the x
		private int maxY;   //Boundary for dragging on the y

		private static final int LINE_THICKNESS = 2;

		/** Constructor: an instance at (x, y) of diameter d and colored black
		 * that represents r and is not filled.
		 * @param r - the Object this circle represents
		 * @param x - x coordinate of center
		 * @param y - y coordinate of center
		 * @param d - the diameter of the circle
		 */
		public Circle(V v, int x, int y, int d) {
			this(v, x, y, d, null, false);
		}

		/** Constructor: an instance at (x, y) of diameter d and colored c
		 * that represents r and is filled iff filled.
		 * @param x - x coordinate of center
		 * @param y - y coordinate of center
		 * @param d - the diameter of the circle
		 * @param c - the Color of this circle - if null, uses the default color for represents
		 * @param filled - whether or not this circle is filled when it is drawn
		 */
		public Circle(V r, int x, int y, int d, Color c, boolean filled) {
			represents = r;
			lines = new HashSet<Line>();

			//Set preliminary bounds
			setBounds(0,0,DEFAULT_DIAMETER + PANEL_BUFFER, DEFAULT_DIAMETER + PANEL_BUFFER);

			setDiameter(d);
			setX1(x);
			setY1(y);

			if (c != null)
				color = c;
			else {
				color = Color.black;
			}
			this.filled = filled;
			setOpaque(false);

			MouseListener clickListener = new MouseListener(){

				/** When clicked, store the initial point at which this is clicked. */
				@Override
				public void mousePressed(MouseEvent e) {
					maxX = GraphFrame.this.getContentPane().getWidth();
					maxY = GraphFrame.this.getContentPane().getHeight();
					clickPoint = e.getPoint();
				}

				@Override
				public void mouseClicked(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {}

				@Override
				public void mouseEntered(MouseEvent e) {}

				@Override
				public void mouseExited(MouseEvent e) {}	
			};

			MouseMotionListener motionListener = new MouseMotionListener(){

				/** When this is dragged, perform the translation from the point
				 * where this was clicked to the new dragged point. */
				@Override
				public void mouseDragged(MouseEvent e) {
					Point p = e.getPoint();
					int newX = Math.min(maxX, Math.max(0, getX1() + p.x - clickPoint.x));
					int newY = Math.min(maxY, Math.max(0, getY1() + p.y - clickPoint.y));
					setX1(newX);
					setY1(newY);
				}

				@Override
				public void mouseMoved(MouseEvent e) {}

			};

			addMouseListener(clickListener);
			addMouseMotionListener(motionListener);

		}

		/** Return the x coordinate of this circle. */
		public int getX1() {
			return x1;
		}

		/** Set the x coordinate of this circle to x.
		 * Also set the bounds such that this circle will be visible upon drawing. */
		public void setX1(int x) {
			x1 = x;
			fixBounds();
			for(Line l : lines){
				l.fixBounds();
			}
		}

		/** Return the y coordinate of this circle. */
		public int getY1() {
			return y1;
		}

		/** Set the y coordinate of this circle to y*/
		public void setY1(int y) {
			y1 = y;
			fixBounds();
			for(Line l : lines){
				l.fixBounds();
			}
		}

		/** Return the color of this circle. */
		public Color getColor() {
			return color;
		}

		/** Set the color of this circle to c.*/
		public void setColor(Color c) {
			color = c;
		}

		/** Return the diameter field of this circle. */
		public int getDiameter() {
			return diameter;
		}

		/** Return the diameter field of this circle to d and
		 * update the bounds (expands outward from center) */
		protected void setDiameter(int d) {
			diameter = d;
			fixBounds();
		}

		/** Extra height added (either on top or bottom) to bounds to fit text */
		private static final int TEXT_HEIGHT = 15;

		/** Extra width added to bounds to fit text */
		private static final int TEXT_WIDTH = 10;

		/** Fixe the boundaries so that all drawings will be within the bounds.
		 * Call after x, y, or diameter is changed. */
		private void fixBounds() {
			int x = getX1();
			int y = getY1();
			int d = getDiameter();
			int dP = d + PANEL_BUFFER;
			setBounds(x - dP/2, y - dP/2, dP, dP);
			Rectangle oldBounds = getBounds();
			setBounds(oldBounds.x, oldBounds.y - TEXT_HEIGHT,
					oldBounds.width + TEXT_WIDTH, oldBounds.height + TEXT_HEIGHT);
			GraphFrame.this.repaint();
		}

		/** Switch this Circle's location with the location of c */
		protected void switchLocation(Circle c) {
			int x2 = c.getX1();
			int y2 = c.getY1();

			c.setX1(x1);
			c.setY1(y1);

			setX1(x2);
			setY1(y2);
		}

		/** Return the distance between the centers of this Circle and c. */
		public double getDistance(Circle c) {
			return Math.sqrt( (Math.pow(x1-c.getX1(), 2) + Math.pow(y1 - c.getY1(), 2) ) );
		}

		/** Return true iff the this circle has the same center as c */
		public boolean locationEquals(Circle c) {
			return x1 == c.x1 && y1 == c.y1;
		}

		/** Return a string representation of this circle. */
		@Override
		public String toString() {
			return "("+ (getX1()-getDiameter()/2) + "," + (getY1()-getDiameter()/2) + 
					") , d=" + getDiameter() + " " + represents.toString();
		}

		/**Draw the Circle when the component is painted. */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;

			g2d.setStroke(new BasicStroke(LINE_THICKNESS));
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int heightPlus = TEXT_HEIGHT;

			Ellipse2D.Double circle2d = new Ellipse2D.Double(PANEL_BUFFER/2, 
					PANEL_BUFFER/2 + heightPlus, getDiameter(), getDiameter());
			g2d.setColor(getColor());
			if (filled) g2d.fill(circle2d);
			g2d.draw(circle2d);
			g2d.drawString(represents.toString(), PANEL_BUFFER, PANEL_BUFFER);

		}

		/** Return a bounding square (of size diameter * diameter) of the circle. */
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(getDiameter(), getDiameter());
		}
	}

	/** This graphics class  allows the drawing of lines.
	 * Lines use a (c1, c2) coordinate system, where c1 and c2 are circle objects
	 * that denote the endpoints of this line.
	 * Each circle has (x, y) coordinates, so a Line can be though of as having
	 * (x1, y1, x2, y2).
	 * @author MPatashnik
	 */
	class Line  extends JPanel{

		private static final long serialVersionUID = -1688624827819736589L;

		/** Default thickness of lines when they are drawn on the GUI */
		public static final int LINE_THICKNESS = 2;

		/** Default color of lines when they are drawn on the GUI */
		public final Color DEFAULT_COLOR = Color.DARK_GRAY;

		private Circle c1;  //Endpoint one of this line
		private Circle c2;  //Endpoint two of this line
		
		boolean arrowEnd1;
		boolean arrowEnd2;

		private Color color; //The color to draw this line; should stay in sync with the color policy

		private Graph<V,E>.Edge represents; //The Edge this represents

		/** Constructor: a line from c1 to c2 representing r and colored according
		 * to the color policy.
		 * @param c1 - the Circle that marks the first end of this line
		 * @param c2 - the Circle that marks the second end of this line
		 * @param r - the Edge this Line represents when drawn on the GUI
		 */
		public Line(Circle c1, Circle c2, Graph<V,E>.Edge r) {
			setBounds(0, 0, GraphFrame.this.getContentPane().getWidth(), 
					GraphFrame.this.getContentPane().getHeight());
			setC1(c1);
			setC2(c2);
			represents = r;
			fixBounds();
			setOpaque(false);
		}

		/** Return the first end of this line. */
		public Circle getC1() {
			return c1;
		}

		/** Sets the first end of this line to c */
		protected void setC1(Circle c) {
			c1 = c;
		}

		/** Return the second end of this line. */
		public Circle getC2() {
			return c2;
		}

		/** Sets the second end of this line to c. */
		protected void setC2(Circle c) {
			c2 = c;
		}

		/** Return the x coordinate of the first end of this line. */
		public int getX1() {
			return c1.getX1();
		}

		/** Return the y coordinate of the first end of this line. */
		public int getY1() {
			return c1.getY1();
		}

		/** Return the x coordinate of the second end of this line. */
		public int getX2() {
			return c2.getX1();
		}

		/** Return the y coordinate of the second end of this line. */
		public int getY2() {
			return c2.getY1();
		}

		/** Return the midpoint of this line. */
		public Point getMid() {
			return new Point(getXMid(), getYMid());
		}

		/** Return the x value of the midpoint of this line. */
		public int getXMid() {
			return (c1.getX1() + c2.getX1()) / 2;
		}

		/** Return the y value of the midpoint of this line. */
		public int getYMid() {
			return (c1.getY1() + c2.getY1()) / 2;
		}

		/** Dynamically resize the drawing boundaries of this line based on the
		 * height and width of the line, with a minimum sized box of.
		 * Call whenever circles move to fix the drawing boundaries of this. */
		public void fixBounds() {
			setBounds(0, 0, GraphFrame.this.getContentPane().getWidth(), 
					GraphFrame.this.getContentPane().getHeight());
			GraphFrame.this.repaint();
		}

		/** Return the current color of this line, which is determined by the color policy. */
		public Color getColor() {
			return color;
		}

		/** Number of pixels of tolerance for a point to be considered on the line */
		public static final int ON_LINE_TOLERANCE = 20;

		/** Return true iff Point p is within ON_LINE_TOLERANCE pixels of this line. */
		public boolean isOnLine(Point p) {
			double dist = distanceTo(p);
			return dist <= (double)ON_LINE_TOLERANCE;
		}

		/** Return the distance from p to this line. */
		public double distanceTo(Point p) {
			return Line2D.ptLineDist(c1.getX1(), c1.getY1(), c2.getX1(), c2.getY1(), p.getX(), p.getY());
		}

		/** Return true iff l intersects this line.
		 * (Return false if they share an endpoint.) */
		public boolean intersects(Line l) {
			return !c1.locationEquals(l.getC1()) && !c1.locationEquals(l.getC2()) &&
					!c2.locationEquals(l.getC1()) && !c2.locationEquals(l.getC2()) &&
					Line2D.linesIntersect(c1.getX1(), c1.getY1(), c2.getX1(), c2.getY1(),
							l.getX1(), l.getY1(), l.getX2(), l.getY2());
		}

		/** Return a String representation of this line */
		@Override
		public String toString() {
			return "(" + c1.getX1() +"," + c1.getY1() + "), (" + 
					c2.getX1() + "," + c2.getY1() + ")";
		}

		/** Paint this line */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(LINE_THICKNESS));
			
			double diffX = (getX2() - getX1());
			double diffY = (getY2() - getY1());
			double hypotenuse = Math.sqrt(diffX * diffX + diffY * diffY);
			double angle = Math.acos(diffX/hypotenuse);
			if(getY2() < getY1())
				angle = 2*Math.PI - angle;
			
			double y1 = getY1() + Math.sin(angle)*c1.diameter/2 + 1;
			double x1 = getX1() + Math.cos(angle)*c1.diameter/2 + 1;
			double y2 = getY2() - Math.sin(angle)*c2.diameter/2;
			double x2 = getX2() - Math.cos(angle)*c2.diameter/2;
						
			Line2D line2d = new Line2D.Double(x1, y1, x2, y2);
			g2d.setColor(getColor());
			g2d.draw(line2d);
			g2d.drawString(represents._2.toString(), 0, 0);
		}


		/** Return the size of the line, as a rectangular bounding box (x2 - x1, y2 - y1). */
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(Math.abs(getX2()-getX1()), Math.abs(getY2()- getY1()));
		}
	}

}
