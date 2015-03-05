package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import common.tuple.Tuple3;

public class AbsGraph<V,E>{

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

		protected Edge(AbsGraph<V, E>.Vertex first, E second,
				AbsGraph<V, E>.Vertex third) {
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

	public Set<V> vertexSet() {
		return new HashSet<V>(vertices.keySet());
	}

	public Set<E> edgeSet() {
		return new HashSet<E>(edges.keySet());
	}

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


	public boolean addEdge(V source, V sink, E e, boolean overwrite) {
		if(! vertices.containsKey(source) || ! vertices.containsKey(sink))
			throw new IllegalArgumentException("Can't connect " + source + "," + sink +
					" in " + this + "- not in graph");

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

}
