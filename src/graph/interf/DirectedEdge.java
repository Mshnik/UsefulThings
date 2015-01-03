package graph.interf;

/** Represents a directed edge - one with a set source and sink
 * 
 * @author MPatashnik
 *
 */
public interface DirectedEdge extends Edge {

	/** Returs the source of this Edge */
	public Vertex getSource();
	
	/** Returns the sink of this Edge */
	public Vertex getSink();
}
