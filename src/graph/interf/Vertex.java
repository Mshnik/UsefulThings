package graph.interf;

import java.util.Set;

/** Designates that instances of the implementing Class represents vertices in a Graph */
public interface Vertex{
	
	/** Returns the set of Edges that leave this Vertex
	 * (For all Edge e in this set, e.source() == this).
	 * 
	 * Return a safe set, so altering the set returned by getEdges() won't
	 * change the result of getEdges(). This is useful for the construction
	 * of residual graphs.
	 */
	public Set<? extends Edge> getOutEdges();
	
	/** Returns the set of Edges that enter this Vertex
	 * (For all Edge e in this set, e.source() == this).
	 * 
	 * Return a safe set, so altering the set returned by getEdges() won't
	 * change the result of getEdges(). This is useful for the construction
	 * of residual graphs.
	 */
	public Set<? extends Edge> getInEdges();
}
