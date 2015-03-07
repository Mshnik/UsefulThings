package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import common.tuple.Tuple3;

/** Represents a Graph - a relational data structure.
 * All relations of which vertices possess which edges, etc are 
 * maintained internally within the Graph to fully encapsulate the data structure.
 * <br><br>
 * The only main requirement made on use of the Graph class is that 
 * every vertex and edge that is stored within the graph needs to be a *unique*
 * instance of V and E, respectively. This is due to the use of HashMaps
 * for storage, thus multiple edges can't be created from the same E instance.
 * 
 * @author Mshnik
 *
 * @param <V> Generic type representing vertices
 * @param <E> Generic type representing edges
 */
public class Graph<V,E> implements Cloneable{

	protected class Vertex{
		protected HashMap<E, Edge> outEdges;
		protected HashMap<E, Edge> inEdges;
		public final V v;

		protected Vertex(V v){
			this.v = v;
			outEdges = new HashMap<>();
			inEdges = new HashMap<>();
		}
		
		public boolean equals(Object o){
			try{
				@SuppressWarnings("unchecked")
				Vertex vertex = (Graph<V,E>.Vertex)o;
				return v.equals(vertex.v);
			}catch(ClassCastException ce){
				return false;
			}
		}
		
		public int hashCode(){
			return v.hashCode();
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

	private boolean directed;
	private HashMap<V, Vertex> vertices;
	private HashMap<E, Edge> edges;

	public Graph(){
		vertices = new HashMap<>();
		edges = new HashMap<>();
		directed = true;
	}
	
	/** Returns a new Graph that is a copy of this.
	 * The two graphs contain the same elements but are completely independent
	 * in terms of underlying structure, so modifications to this Graph
	 * won't alter the returned graph, and modifications to the returned
	 * graph won't alter this graph.
	 */
	public Graph<V, E> clone(){
		Graph<V, E> g = new Graph<V, E>();
		g.setDirected(directed);
		
		for(V v : vertexSet()){
			g.addVertex(v);
		}
		for(E e : edgeSet()){
			Edge edge = edges.get(e);
			g.addEdge(edge.getSource().v, edge.getSink().v, e);
		}
		
		return g;
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
	
	public boolean isDirected(){
		return directed;
	}
	
	public void setDirected(boolean directed){
		this.directed = directed;
	}

	/** Adds the given vertex to the graph with no edges.
	 * If the vertex is already in the graph, do nothing
	 * Returns true if the an operation is performed this way, false otw.
	 */
	public boolean addVertex(V v) {
		if(vertices.containsKey(v))
			return false;

		vertices.put(v, new Vertex(v));
		return true;
	}

	/** Connects the two given vertices in the graph by adding an edge.
	 * Returns true if an operation is performed this way, false otw.
	 * @throws NotInGraphException - if source or sink are not vertices in this graph
	 */
	public boolean addEdge(V source, V sink, E e) throws NotInGraphException{
		if(! vertices.containsKey(source) || ! vertices.containsKey(sink))
			throw new NotInGraphException("Can't create edge " + e, source, sink);
		if(edges.containsKey(e))
			return false;
		
		Vertex sourceV = vertices.get(source);
		Vertex sinkV = vertices.get(sink);

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
	 * For undirected graphs, returns any edge that connects the two, in either direction
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
		if(! directed){
			for(E e : sourceV.inEdges.keySet()){
				if(sinkV.outEdges.containsKey(e)){
					return e;
				}
			}
		}
		return null;
	}
	
	/** Returns true iff there is an edge with the given source and sink.
	 * For undirected graphs, returns true iff there is any edge that conencts the two, in either direction
	 * @throws NotInGraphException - if source or sink are not vertices in this graph*/
	public boolean isConnected(V source, V sink) throws NotInGraphException{
		return getConnection(source, sink) != null;
	}
	
	/** Returns the vertex at the other end of the given edge.
	 * Returns null if oneEnd is neither end of e.
	 * @throws NotInGraphException if e isn't an edge in this graph
	 */
	public V getOther(E e, V oneEnd){
		if(! edges.containsKey(e))
			throw new NotInGraphException("Can't find other endpoint of edge", e);
		Edge edge = edges.get(e);
		
		if(edge._1.v.equals(oneEnd)) return edge._3.v;
		if(edge._3.v.equals(oneEnd)) return edge._1.v;
		return null;
	}
	
	/** Returns a set of all edges with v as an endpoint (source or sink)
	 * @throws NotInGraphException if v is not in this graph */
	public Set<E> edgeSetOf(V v){
		if(! vertices.containsKey(v))
			throw new NotInGraphException("Can't get source and sink set", v);
		HashSet<E> e = new HashSet<E>(vertices.get(v).outEdges.keySet());
		e.addAll(vertices.get(v).inEdges.keySet());
		return e;
	}
	
	/** Returns a set of all edges with v as a source.
	 * If this graph is undirected returns edgeSetOf(source) instead, as
	 * every vertex is source and sink 
	 * @throws NotInGraphException if source is not in this graph */
	public Set<E> edgeSetOfSource(V source){
		if(! vertices.containsKey(source))
			throw new NotInGraphException("Can't get source set", source);
		if(directed)
			return new HashSet<E>(vertices.get(source).outEdges.keySet());
		else
			return edgeSetOf(source);
	}
	
	/** Returns a set of all edges with v as a sink.
	 * If this graph is undirected returns edgeSetOf(source) instead, as
	 * every vertex is source and sink
	 * @throws NotInGraphException if sink is not in this graph */
	public Set<E> edgeSetOfSink(V sink){
		if(! vertices.containsKey(sink))
			throw new NotInGraphException("Can't get sink set", sink);
		if(directed)
			return new HashSet<E>(vertices.get(sink).inEdges.keySet());
		else
			return edgeSetOf(sink);
	}
	
	/** Returns the vertices on either end of the given edge - an arrayList of length 2
	 * @throws NotInGraphException if e is not in this graph */
	public ArrayList<V> verticesOf(E e){
		if(! edges.containsKey(e))
			throw new NotInGraphException("Can't get verticies of", e);
		Edge edge = edges.get(e);
		ArrayList<V> a = new ArrayList<V>();
		a.add(edge.getSource().v);
		a.add(edge.getSink().v);
		return a;
	}
	
	/** Returns all neighbor vertices to {@code v}. In a directed graph
	 * this is the set of vertices {@code a in A} for which there exists an edge e
	 * with v as the source and a as the sink. In an undirected graph,
	 * this is the set of vertices for which there exists an edge e with v as
	 * either the source or the sink and a as the other endpoint.
	 */
	public Set<V> neighborsOf(V v) throws NotInGraphException{
		if(! vertices.containsKey(v))
			throw new NotInGraphException("Can't get neighbor set", v);
		
		Vertex vertex = vertices.get(v);
		HashSet<V> neighbors = new HashSet<>();
		for(Edge e : vertex.outEdges.values()){
			neighbors.add(e.getSink().v);
		}
		if(! directed){
			for(Edge e : vertex.inEdges.values()){
				neighbors.add(e.getSource().v);
			}
		}
		return neighbors;
	}

	@SuppressWarnings("serial")
	public static class NotInGraphException extends RuntimeException{
		public NotInGraphException(String msg, Object... objects){
			super(msg + " - " + Arrays.deepToString(objects) + 
					(objects.length > 1 ? "aren't" : "isn't") + " contained in graph");
		}
	}

}
