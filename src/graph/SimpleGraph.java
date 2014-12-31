package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SimpleGraph implements Graph {

	private HashMap<Vertex, EdgeContainer> graph;
	
	private Set<Edge> edges; // A copy data structure to support getting edgeSet. Calculated lazily.
	                         //If in conflict with graph, graph is correct, not this.
	                         //Should be reset to null whenever the graph is altered
	
	/** Holds two sets for a vertex - edges leaving, and edges entering.
	 * Use addEdge to correctly add to either of sets, and to catch errors. */
	private class EdgeContainer{
		final Vertex v;
		HashSet<Edge> inSet;
		HashSet<Edge> outSet;
		
		private EdgeContainer(Vertex v){
			this.v = v;
			inSet = new HashSet<Edge>();
			outSet = new HashSet<Edge>();
		}
		
		/** Adds Edge e to the correct set. Throws exception if v isn't an endpoint of e */
		private void addEdge(Edge e) throws IllegalArgumentException{
			if(e.getSource() == v)
				outSet.add(e);
			else if(e.getSink() == v)
				inSet.add(e);
			else
				throw new IllegalArgumentException(v + " is not an endpoint of " + e);
		}
		
		/** Removes Edge e. Returns true if the edge was removed, false if it was never there */
		private boolean removeEdge(Edge e){
			if(e.getSource() == v){
				outSet.remove(e);
			    return true;
			}
			if(e.getSink() == v){
				inSet.remove(e);
			    return true;
			}
			return false;
		}
	}
	
	@Override
	public Set<Vertex> vertexSet() {
		return graph.keySet();
	}

	@Override
	public Set<Edge> edgeSet() {
		if(edges == null){
			edges = new HashSet<Edge>();
			for(Vertex v : vertexSet()){
				edges.addAll(outSet(v)); //Just add out set - every edge is the out of one vertex.
			}
		}
		return edges;
	}

	@Override
	public Set<Edge> outSet(Vertex v) {
		return graph.get(v).outSet;
	}

	@Override
	public Set<Edge> inSet(Vertex v) {
		return graph.get(v).inSet;
	}

	@Override
	public boolean addVertex(Vertex v, boolean overwrite) {
		//Check if vertex is already present and we aren't overwriting.
		if(graph.containsKey(v) && ! overwrite)
			return false;
		
		//If we are overwriting, remove the old vertex
		if(overwrite)
		  removeVertex(v);
		
		EdgeContainer eC = new EdgeContainer(v);
		graph.put(v, eC);
		return true;
	}

	@Override
	public boolean addEdge(Edge e, boolean overwrite) {
	    //Add endpoints to graph, but don't overwrite if they are there
		Vertex source = e.getSource();
		Vertex sink = e.getSink();
		addVertex(source, false);
		addVertex(sink, false);
		
		if(outSet(source).contains(e) && inSet(sink).contains(e) && ! overwrite)
			return false;
		
		//Put the edge in
		graph.get(source).addEdge(e);
		graph.get(sink).addEdge(e);
		
		return true;
	}

	@Override
	public boolean removeVertex(Vertex v) {
		if(! graph.containsKey(v))
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
