package graph.interf;

import java.util.Set;

public interface Graph<V, E> {

	/** Returns the vertices in this graph */
	public Set<V> vertexSet();
	
	/** Returns the edges in this graph */
	public Set<E> edgeSet();
	
	/** Returns the set of edges that leave the given vertex */
	public Set<E> outSet(V v);
	
	/** Returns the set of edges that enter the given vertex */
	public Set<E> inSet(V v);
	
	/** Adds the given vertex to the graph with no edges.
	 * If the vertex is already in the graph, only overwrite if overwrite is true
	 * When overwriting, clear edges from this vertex, leaving an unconnected vertex.
	 * Returns true if the an operation is performed this way, false otw.
	 */
	public boolean addVertex(V v, boolean overwrite);
	
	/** Connects the two given vertices in the graph by adding an edge.
	 * If the vertex is already in the graph, do nothing, only overwrite if overwrite is true.
	 * Adds its endpoints to the graph if they aren't already there, but doesn't overwrite in doing this.
	 *  (Old edges from source -> other, etc... persist)
	 * Returns true if an operation is performed this way, false otw.
	 */
	public boolean addEdge(E e, boolean overwrite);
	
	/** Removes this vertices (And all of the edges going in and out of it) from the graph.
	 * Returns true if the vertex was removed this way, false if there was no vertex to remove.
	 */
	public boolean removeVertex(V v);
	
	/** Removes this edge from the graph.
	 * Returns true if the edge was removed this way, false if there was no edge to remove.
	 */
	public boolean removeEdge(E e);
	
	
}
