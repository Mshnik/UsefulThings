package graph;

import java.util.HashMap;

import common.dataStructures.NotInCollectionException;

/** SimpleGraph is a simple extension of the graph class.
 * SimpleVertex and SimpleEdge classes are internally created to 
 * manage the relations. Basically, SimpleGraph is useful when 
 * the generic nature of the Graph class is unneeded. <br><br>
 * As the SimpleEdge class implements Flowable and Weighted,
 * SimpleGraph is appropriate for shortest path and max flow problems.
 * @author Mshnik
 *
 */
public class SimpleGraph extends Graph<SimpleVertex, SimpleEdge> {

	private HashMap<Integer, SimpleVertex> idToVertex;
	private HashMap<Integer, SimpleEdge> idToEdge;

	/** Constructs a new (empty) directed SimpleGraph */
	public SimpleGraph(){
		super();
		idToVertex = new HashMap<>();
		idToEdge = new HashMap<>();
	}

	/** Constructs a new (empty) SimpleGraph with the given directed-ness */
	public SimpleGraph(boolean directed){
		super(directed);
		idToVertex = new HashMap<>();
		idToEdge = new HashMap<>();
	}

	/** Constructs a new SimpleGraph that is a copy of the given graph
	 * The two graphs contain the same elements but are completely independent
	 * in terms of underlying structure, so modifications to this Graph
	 * won't alter the returned graph, and modifications to the returned
	 * graph won't alter this graph.<br><br>
	 * Moreover, the graph is deeply copied - the SimpleVertices and SimpleEdges
	 * in g are cloned before being added to this. This is a difference between
	 * the SimpleGraph class and the Graph class.
	 */
	public SimpleGraph(Graph<SimpleVertex, SimpleEdge> g){
		super(g.isDirected());
		for(SimpleVertex v : g.vertexSet()){
			addVertex(v.clone());
		}
		for(SimpleEdge e : g.edgeSet()){
			Edge edge = g.getEdge(e);
			addEdge(edge.getSource().v, edge.getSink().v, e.clone());
		}
	}

	/** Returns a copy of this SimpleGraph.
	 */
	@Override
	public SimpleGraph clone(){
		return new SimpleGraph(this);
	}

	/** Adds the given vertex to the graph with no edges.
	 * If the vertex is already in the graph, do nothing and return false
	 * Returns true if the an operation is performed this way, false otw.
	 */
	@Override
	public boolean addVertex(SimpleVertex v){
		boolean added = super.addVertex(v);
		if(added) idToVertex.put(v.getID(), v);
		return added;
	}

	/** Connects the two given vertices in the graph by adding an edge.
	 * If there is already a connection from source to sink 
	 * (or sink to source and this is undirected), or e is already
	 * an edge in this graph, do nothing and return false.
	 * Returns true if an operation is performed this way, false otw.
	 * @throws NotInCollectionException - if source or sink are not vertices in this graph
	 */
	@Override
	public boolean addEdge(SimpleVertex v1, SimpleVertex v2, SimpleEdge e)
			throws NotInCollectionException{
		boolean added = super.addEdge(v1, v2, e);
		if(added) idToEdge.put(e.getID(), e);
		return added;
	}

	/** Adds a new vertex to the graph with no edges. 
	 * This will always result in a new vertex being added.
	 * @return the ID of the added vertex
	 */
	public int addVertex(){
		SimpleVertex s = new SimpleVertex();
		addVertex(s);
		return s.getID();
	}

	/** Adds a new Edge to the graph, connecting source to sink.
	 * If source and sink are already connected by an existing edge, 
	 * does nothing instead.
	 * @param source - the source of the new edge
	 * @param sink - the sink of the new edge.
	 * @return the ID of the created edge if source and sink weren't connected before,
	 * or -1 if they were already connected
	 * @throws NotInCollectionException - if source or sink are not vertices in this graph
	 */
	public int addEdge(SimpleVertex source, SimpleVertex sink) 
			throws NotInCollectionException{
		SimpleEdge e = new SimpleEdge(0,0);
		boolean added = addEdge(source, sink, e);
		return added ? e.getID() : -1;
	}

	/** Removes this vertices (And all of the edges going in and out of it) from the graph.
	 * Returns true if the vertex was removed this way, false if there was no vertex to remove.
	 */
	@Override
	public boolean removeVertex(SimpleVertex v){
		boolean removed = super.removeVertex(v);
		if(removed) idToVertex.remove(v.getID());
		return removed;
	}

	/** Removes this edge from the graph.
	 * Returns true if the edge was removed this way, false if there was no edge to remove.
	 */
	@Override
	public boolean removeEdge(SimpleEdge e){
		boolean removed = super.removeEdge(e);
		if(removed) idToEdge.remove(e.getID());
		return removed;
	}

	/** Removes the vertex in this graph with the given id.
	 * @param id - the id of vertex to remove
	 * @return true if a vertex was removed this way, false otherwise.
	 */
	public boolean removeVertex(int id){
		if(! idToVertex.containsKey(id)) return false;
		super.removeVertex(idToVertex.get(id));
		return true;
	}

	/** Removes the edge in this graph with the given id.
	 * @param id - the id of edge to remove
	 * @return true if a edge was removed this way, false otherwise.
	 */
	public boolean removeEdge(int id){
		if(! idToEdge.containsKey(id)) return false;
		super.removeEdge(idToEdge.get(id));
		return true;
	}

	/** Clears this graph entirely, removing all edges and vertices. */
	@Override
	public void clear(){
		super.clear();
		idToVertex.clear();
		idToEdge.clear();
	}

}
