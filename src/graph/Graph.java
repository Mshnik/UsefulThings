package graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import common.tuple.Tuple3;

public class Graph<V,E>{

	protected class Vertex{
		protected HashMap<E, Edge> outEdges;
		protected HashMap<E, Edge> inEdges;
		public final V v;

		protected Vertex(V v){
			this.v = v;
			outEdges = new HashMap<>();
			inEdges = new HashMap<>();
		}
	}

	protected class Edge extends Tuple3<Vertex, E, Vertex>{

		protected Edge(Graph<V, E>.Vertex first, E second,
				Graph<V, E>.Vertex third) {
			super(first, second, third);
		}

		public Vertex getSource(){
			return _1;
		}

		public Vertex getSink(){
			return _3;
		}
	}

	private HashMap<V, Vertex> vertices;
	private HashMap<E, Edge> edges;

	public Graph(){
		vertices = new HashMap<>();
		edges = new HashMap<>();
	}

	public Set<V> vertexSet() {
		return new HashSet<V>(vertices.keySet());
	}

	public int vertexSize(){
		return vertices.size();
	}

	public Set<E> edgeSet() {
		return new HashSet<E>(edges.keySet());
	}

	public int edgeSize(){
		return edges.size();
	}

	/** Adds the given vertex to the graph with no edges.
	 * If the vertex is already in the graph, does not overwrite
	 * Returns true if the an operation is performed this way, false otw.
	 */
	public boolean addVertex(V v){
		return addVertex(v, false);
	}

	/** Adds the given vertex to the graph with no edges.
	 * If the vertex is already in the graph, only overwrite if overwrite is true
	 * When overwriting, clear edges from this vertex, leaving an unconnected vertex.
	 * Returns true if the an operation is performed this way, false otw.
	 */
	public boolean addVertex(V v, boolean overwrite) {
		//Check if vertex is already present and we aren't overwriting.
		if(vertices.containsKey(v) && ! overwrite)
			return false;

		//If we are overwriting, remove the old vertex
		if(overwrite)
			removeVertex(v);

		vertices.put(v, new Vertex(v));
		return true;
	}

	/** Connects the two given vertices in the graph by adding an edge.
	 * Does not overwrite.
	 *  (Old edges from source -> other, etc... persist)
	 * Returns true if an operation is performed this way, false otw.
	 */
	public boolean addEdge(V source, V sink, E e){
		return addEdge(source, sink, e, false);
	}

	/** Connects the two given vertices in the graph by adding an edge.
	 * If the vertex is already in the graph, do nothing, only overwrite if overwrite is true.
	 *  (Old edges from source -> other, etc... persist)
	 * Returns true if an operation is performed this way, false otw.
	 * @throws NotInGraphException - if source or sink are not vertices in this graph
	 */
	public boolean addEdge(V source, V sink, E e, boolean overwrite) 
			throws NotInGraphException{
		if(! vertices.containsKey(source) || ! vertices.containsKey(sink))
			throw new NotInGraphException("Can't create edge " + e, source, sink);

		Vertex sourceV = vertices.get(source);
		Vertex sinkV = vertices.get(sink);

		if(!overwrite && (sourceV.outEdges.containsKey(e) || sinkV.inEdges.containsKey(e)))
			return false;

		if(overwrite)
			removeEdge(e);

		//Put the edge in
		Edge edge = new Edge(sourceV, e, sinkV);
		edges.put(e, edge);
		sourceV.outEdges.put(e, edge);
		sinkV.inEdges.put(e, edge);
		return true;
	}

	/** Removes this vertices (And all of the edges going in and out of it) from the graph.
	 * Returns true if the vertex was removed this way, false if there was no vertex to remove.
	 */
	public boolean removeVertex(V v) {
		if(! vertices.containsKey(v))
			return false;

		Vertex vertex = vertices.get(v);
		//Remove all connections to/from v
		for(E e : vertex.inEdges.keySet())
			removeEdge(e);
		for(E e : vertex.outEdges.keySet())
			removeEdge(e);

		//Actually remove the vertex
		vertices.remove(v);
		return true;
	}

	/** Removes this edge from the graph.
	 * Returns true if the edge was removed this way, false if there was no edge to remove.
	 */
	public boolean removeEdge(E e) {
		if(! edges.containsKey(e))
			return false;

		Edge edge = edges.get(e);
		Vertex source = edge.getSource();
		Vertex sink = edge.getSink();

		source.outEdges.remove(e);
		sink.inEdges.remove(e);
		edges.remove(e);
		return true;
	}

	/** Returns the edge with the given source and sink, if any. null otw.
	 * @throws NotInGraphException - if source or sink are not vertices in this graph*/
	public E getConnection(V source, V sink) throws NotInGraphException{
		if(! vertices.containsKey(source) || ! vertices.containsKey(sink))
			throw new NotInGraphException("Can't check for connection", source, sink);

		Vertex sourceV = vertices.get(source);
		Vertex sinkV = vertices.get(sink);
		for(E e : sourceV.outEdges.keySet()){
			if(sinkV.inEdges.containsKey(e)){
				return e;
			}
		}
		
		return null;
	}
	
	/** Returns the vertex at the other end of the given edge.
	 * Returns null if oneEnd is neither end of e.
	 * @throws NotInGraphException if e isn't an edge in this graph
	 */
	public V getOther(E e, V oneEnd){
		if(! edges.containsKey(e))
			throw new NotInGraphException("Can't find other endpoint of edge", e);
		Edge edge = edges.get(e);
		
		if(edge._1.equals(oneEnd)) return edge._3.v;
		if(edge._3.equals(oneEnd)) return edge._1.v;
		return null;
	}
	
	/** Returns a set of all edges with v as a source or sink */
	public Set<E> edgeSetOf(V v){
		if(! vertices.containsKey(v))
			throw new NotInGraphException("Can't get source and sink set", v);
		HashSet<E> e = new HashSet<E>(vertices.get(v).outEdges.keySet());
		e.addAll(vertices.get(v).inEdges.keySet());
		return e;
	}
	
	/** Returns a set of all edges with v as a source */
	public Set<E> edgeSetOfSource(V source){
		if(! vertices.containsKey(source))
			throw new NotInGraphException("Can't get source set", source);
		return new HashSet<E>(vertices.get(source).outEdges.keySet());
	}
	
	/** Returns a set of all edges with v as a sink */
	public Set<E> edgeSetOfSink(V sink){
		if(! vertices.containsKey(sink))
			throw new NotInGraphException("Can't get sink set", sink);
		return new HashSet<E>(vertices.get(sink).inEdges.keySet());
	}

	@SuppressWarnings("serial")
	public static class NotInGraphException extends RuntimeException{
		public NotInGraphException(String msg, Object... objects){
			super(msg + " - " + Arrays.deepToString(objects) + 
					(objects.length > 1 ? "aren't" : "isn't") + " contained in graph");
		}
	}

}
