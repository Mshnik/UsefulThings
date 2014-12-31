package graph;

import java.awt.Point;
import java.util.Set;

/** Designates that instances of the implementing Class represents vertices in a Graph */
public interface Vertex {
	
	/** Returns the set of Edges that leave this Vertex
	 * (For all Edge e in this set, e.source() == this).
	 * 
	 * Return a safe set, so altering the set returned by getEdges() won't
	 * change the result of getEdges(). This is useful for the construction
	 * of residual graphs.
	 */
	public Set<Edge> getOutEdges();
	
	/** Returns the set of Edges that enter this Vertex
	 * (For all Edge e in this set, e.source() == this).
	 * 
	 * Return a safe set, so altering the set returned by getEdges() won't
	 * change the result of getEdges(). This is useful for the construction
	 * of residual graphs.
	 */
	public Set<Edge> getInEdges();
	
	/** Maintain a label for use in preflowPush or other algorithms.
	 * Shouldn't change outside of setLabel(). Unitialized value should be unimportant */
	public int getLabel();
	/** @see getLabel() */
	public void setLabel(int i);
	
	/** A Previous vertex. Useful for path finding algorithms. */
	public Vertex getPrevious();
	/** @see getPrevious() */
	public void setPrevious(Vertex v);
	
	/** Coordinates (if any) for this in coordinate space. Useful for GUI applications and the like */
	public Point getPosition();
	/** @see getPosition() */
	public void setPosition(Point p);
	
	/** Returns the graph this belongs to */
	public Graph getGraph();
	
}
