package graph;

import graph.interf.Edge;
import graph.interf.Graph;
import graph.interf.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class AbsGraph<V,E> implements Graph<V, E>{

	private class VPair{
		private final V source;
		private final V sink;
		private VPair(V s1, V s2){
			source = s1;
			sink = s2;
		}
	}

	protected HashMap<VPair, HashSet<E>> graph;
	private HashSet<V> vertices; //Lazily calculated copy of all vertices in graph
	private HashSet<E> edges; //Lazily calculated copy of all edges in graph

	private void refreshVertexSet(){
		if(vertices == null){
			vertices = new HashSet<V>();
			for(VPair v : graph.keySet()){
				vertices.add(v.sink);
				vertices.add(v.source);
			}
		}
	}

	private void refreshEdgeSet(){
		if(edges == null){
			edges = new HashSet<E>();
			for(VPair v : graph.keySet()){
				edges.addAll(graph.get(v));
			}
		}
	}

	@Override
	public Set<V> vertexSet() {
		refreshVertexSet();
		return new HashSet<V>(vertices);
	}

	@Override
	public Set<E> edgeSet() {
		refreshEdgeSet();
		return new HashSet<E>(edges);
	}

	@Override
	public boolean addVertex(V v, boolean overwrite) {
		//Check if vertex is already present and we aren't overwriting.
		if(graph.containsKey(v) && ! overwrite)
			return false;

		//If we are overwriting, remove the old vertex
		if(overwrite)
			removeVertex(v);

		graph.put(v, new HashSet<E>());
		return true;
	}

	@Override
	public E addEdge(V source, V sink, boolean overwrite) {
		//Add endpoints to graph, but don't overwrite if they are there
		if(! graph.containsKey(source) || ! graph.containsKey(sink))
			throw new IllegalArgumentException("Can't connect " + source + "," + sink +
					" in " + this + "- not in graph");

		if(outSet(source).contains(e) && inSet(sink).contains(e) && ! overwrite)
			return false;

		//Put the edge in
		graph.get(source).addEdge(e);
		graph.get(sink).addEdge(e);

		return true;
	}

	@Override
	public boolean removeVertex(Vertex v) {
		if(! vertices.contains(v))
			return false;

		//Remove all connections to/from v
		for(Edge e : inSet(v))
			removeEdge(e);
		for(Edge e : outSet(v))
			removeEdge(e);

		//Actually remove the vertex
		graph.remove(v);

		return true;
	}

	@Override
	public boolean removeEdge(Edge e) {
		Vertex source = e.getSource();
		Vertex sink = e.getSink();

		if(outSet(source).contains(e) || inSet(sink).contains(e)){
			graph.get(source).removeEdge(e);
			graph.get(sink).removeEdge(e);
			return true;
		}
		return false;
	}

}
