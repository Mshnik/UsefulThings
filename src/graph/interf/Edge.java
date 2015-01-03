package graph.interf;

/** Represents an undirected, unweighted edge in a graph */
public interface Edge {
	
	/** Returns the two vertices this edge connects, in a 2x1 array */
	public Vertex[] getVertices();
}
