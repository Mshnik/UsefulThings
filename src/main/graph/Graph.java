package graph;

import java.util.*;
import java.util.function.Supplier;

import common.dataStructures.NotInCollectionException;
import common.types.Tuple;
import common.types.Tuple2;
import common.types.Tuple3;

/**
 * Represents a Graph - a relational data structure.
 * All relations of which vertices possess which edges, etc are
 * maintained internally within the Graph to fully encapsulate the data structure.
 * <br><br>
 * The only main requirement made on use of the Graph class is that
 * every vertex and edge that is stored within the graph needs to be a *unique*
 * instance of V and E, respectively. This is due to the use of HashMaps
 * for storage, thus multiple edges can't be created from the same E instance.
 *
 * @param <V> Generic type representing vertices
 * @param <E> Generic type representing edges
 * @author Mshnik
 */
public class Graph<V, E> implements Cloneable {

  protected class Vertex {
    protected HashMap<E, Edge> outEdges;
    protected HashMap<E, Edge> inEdges;
    public final V v;

    protected Vertex(V v) {
      this.v = v;
      outEdges = new HashMap<>();
      inEdges = new HashMap<>();
    }

    public boolean equals(Object o) {
      try {
        @SuppressWarnings("unchecked")
        Vertex vertex = (Graph<V, E>.Vertex) o;
        return v.equals(vertex.v);
      } catch (ClassCastException ce) {
        return false;
      }
    }

    public int hashCode() {
      return v.hashCode();
    }

    @Override
    public String toString() {
      return v.toString();
    }
  }

  protected class Edge extends Tuple3<Vertex, E, Vertex> {

    protected Edge(Graph<V, E>.Vertex first, E second,
                   Graph<V, E>.Vertex third) {
      super(first, second, third);
    }

    public Vertex getSource() {
      return _1;
    }

    public Vertex getSink() {
      return _3;
    }
  }

  /**
   * An extension of the Graph class that functions as a unmodifiable *view*
   * of an underlying graph.
   * <br><br>
   * This view doesn't support alterations. Any method that would alter this
   * graph is overridden to throw an UnsupportedOperationException when called.
   * Other methods return the result of the underlying graph for the given
   * method.
   * <br><br>
   * Because instances of UnmodifiableGraph are only views, changes in the
   * underlying graph will be reflected by this UnmodifiableGraph.
   * Graph extensions should also extend UnmodifiableGraph so that
   * the extended version can be returned by the unmodifiableGraph()
   * method.
   *
   * @author Mshnik
   */
  protected class UnmodifiableGraph extends Graph<V, E> {

    /**
     * The graph this UnmodifiableGraph is a view of
     */
    private final Graph<V, E> graph;

    private UnmodifiableGraph(Graph<V, E> g) {
      if (g == null) throw new IllegalArgumentException("Can't View Null Graph");
      graph = g;

      //Make sure this Unmodifiable can't personally store vertices or edges.
      //This will cause an NPE if these are ever accessed, which would imply
      //that some method in graph wasn't overridden in UnmodifiableGraph
      this.vertices = null;
      this.edges = null;
    }

    @Override
    public Graph<V, E> clone() {
      return graph.clone();
    }

    /**
     * Returns this, instead of creating another redundant view of the
     * same underlying graph
     */
    @Override
    public UnmodifiableGraph unmodifiableGraph() {
      return this;
    }

    @Override
    public boolean equals(Object o) {
      return graph.equals(o);
    }

    @Override
    public int hashCode() {
      return graph.hashCode();
    }

    @Override
    public Set<V> vertexSet() {
      return graph.vertexSet();
    }

    @Override
    public int vertexSize() {
      return graph.vertexSize();
    }

    @Override
    public boolean containsVertex(V v) {
      return graph.containsVertex(v);
    }

    @Override
    protected Vertex getVertex(V v) {
      return graph.getVertex(v);
    }

    @Override
    public Set<E> edgeSet() {
      return graph.edgeSet();
    }

    @Override
    public boolean containsEdge(E e) {
      return graph.containsEdge(e);
    }

    @Override
    public int edgeSize() {
      return graph.edgeSize();
    }

    @Override
    protected Edge getEdge(E e) {
      return graph.getEdge(e);
    }

    @Override
    public boolean isDirected() {
      return graph.isDirected();
    }

    /**
     * Not Supported by UnmodifiableGraph.
     *
     * @throws UnsupportedOperationException Always
     */
    @Override
    public boolean addVertex(V v) {
      throw new UnsupportedOperationException("Can't Modify UnmodifiableGraph");
    }

    /**
     * Not Supported by UnmodifiableGraph.
     *
     * @throws UnsupportedOperationException Always
     */
    @Override
    public boolean addEdge(V source, V sink, E e) {
      throw new UnsupportedOperationException("Can't Modify UnmodifiableGraph");
    }

    /**
     * Not Supported by UnmodifiableGraph.
     *
     * @throws UnsupportedOperationException Always
     */
    @Override
    public boolean removeVertex(V v) {
      throw new UnsupportedOperationException("Can't Modify UnmodifiableGraph");
    }

    /**
     * Not Supported by UnmodifiableGraph.
     *
     * @throws UnsupportedOperationException Always
     */
    @Override
    public boolean removeEdge(E e) {
      throw new UnsupportedOperationException("Can't Modify UnmodifiableGraph");
    }

    /**
     * Not Supported by UnmodifiableGraph.
     *
     * @throws UnsupportedOperationException Always
     */
    @Override
    public void clear() {
      throw new UnsupportedOperationException("Can't Modify UnmodifiableGraph");
    }

    @Override
    public E getConnection(V source, V sink) {
      return graph.getConnection(source, sink);
    }

    @Override
    public boolean isConnected(V source, V sink) {
      return graph.isConnected(source, sink);
    }

    @Override
    public V getOther(E e, V oneEnd) {
      return graph.getOther(e, oneEnd);
    }

    @Override
    public Set<E> edgeSetOf(V v) {
      return graph.edgeSetOf(v);
    }

    @Override
    public Set<E> edgeSetOfSource(V source) {
      return graph.edgeSetOfSource(source);
    }

    @Override
    public Set<E> edgeSetOfSink(V sink) {
      return graph.edgeSetOfSink(sink);
    }

    @Override
    public int degreeOf(V v) {
      return graph.degreeOf(v);
    }

    @Override
    public int outDegreeOf(V source) {
      return graph.outDegreeOf(source);
    }

    @Override
    public int inDegreeOf(V sink) {
      return graph.inDegreeOf(sink);
    }

    @Override
    public V sourceOf(E e) {
      return graph.sourceOf(e);
    }

    @Override
    public V sinkOf(E e) {
      return graph.sinkOf(e);
    }

    @Override
    public Tuple2<V, V> verticesOf(E e) {
      return graph.verticesOf(e);
    }

    @Override
    public boolean isSelfEdge(E e) {
      return graph.isSelfEdge(e);
    }

    @Override
    public boolean isEndpointOf(E e, V endpoint) {
      return graph.isEndpointOf(e, endpoint);
    }

    @Override
    public V getSharedEndpoint(E e1, E e2) {
      return graph.getSharedEndpoint(e1, e2);
    }

    @Override
    public Set<V> neighborsOf(V v) {
      return graph.neighborsOf(v);
    }

  }

  private boolean directed;
  protected HashMap<V, Vertex> vertices;
  protected HashMap<E, Edge> edges;
  protected UnmodifiableGraph unmodifiableGraph;

  /**
   * Constructs a new graph from the given adjacency list
   * Uses the edgeCreator to create new edges for each connection
   * @param directed
   * @param adjacencyList
   * @param edgeCreator
   * @param <V>
   * @param <E>
   * @return
   */
  public static <V,E> Graph<V,E> ofAdjacencyList(boolean directed, Map<V, List<V>> adjacencyList, Supplier<E> edgeCreator) {
    Graph<V,E> g = new Graph<>(directed);
    for(V v : adjacencyList.keySet()) {
      g.addVertex(v);
    }

    for(V v : adjacencyList.keySet()) {
      for(V v2 : adjacencyList.get(v)) {
        g.addEdge(v, v2, edgeCreator.get());
      }
    }
    return g;
  }

  /**
   * Constructs a new graph from the given adjacency matrix
   * Uses the edgeCreator to create new edges.
   * The list of vertices should implement randomAccess for quick access during creation.
   * @param directed
   * @param vertexOrdering
   * @param connectionMatrix
   * @param edgeCreator
   * @param <V>
   * @param <E>
   * @return
   */
  public static <V,E> Graph<V,E> ofAdjacencyMatrix(boolean directed, List<V> vertexOrdering,
                                                   boolean[][] connectionMatrix, Supplier<E> edgeCreator) {

    assert vertexOrdering.size() == connectionMatrix.length;
    assert vertexOrdering instanceof RandomAccess;

    Graph<V,E> g = new Graph<>();
    for(V v : vertexOrdering) {
      g.addVertex(v);
    }

    for(int i = 0; i < connectionMatrix.length; i++) {
      for(int j = 0; j < connectionMatrix[i].length; j++) {
        if (connectionMatrix[i][j]) {
          g.addEdge(vertexOrdering.get(i), vertexOrdering.get(j), edgeCreator.get());
        }
      }
    }

    return g;
  }

  /**
   * Constructs a new graph that contains no vertices or edges
   *
   * @param directed - true if this is a graph of directed edges, false otherwise
   */
  public Graph(boolean directed) {
    vertices = new HashMap<>();
    edges = new HashMap<>();
    this.directed = directed;
    unmodifiableGraph = null;
  }

  /**
   * Constructs a new graph that contains no vertices or edges
   * and is directed - Calls {@code Graph(true)}
   */
  public Graph() {
    this(true);
  }

  /**
   * Constructs a new graph that is a copy of the given graph
   * The two graphs contain the same elements but are completely independent
   * in terms of underlying structure, so modifications to this Graph
   * won't alter the returned graph, and modifications to the returned
   * graph won't alter this graph.<br><br>
   * However, the graph is shallowly copied - V and E instances are not
   * copied - they will be contained in both this graph and g.
   * If deep copy behavior is necessary for V or E, extend graph re-write this
   * constructor.
   */
  public Graph(Graph<? extends V, ? extends E> g) {
    this(g.directed);
    for (V v : g.vertexSet()) {
      addVertex(v);
    }
    for (E e : g.edgeSet()) {
      Graph<? extends V, ? extends E>.Edge edge = g.edges.get(e);
      addEdge(edge.getSource().v, edge.getSink().v, e);
    }
  }

  /**
   * Returns a new Graph that is a copy of this.
   * The two graphs contain the same elements but are completely independent
   * in terms of underlying structure, so modifications to this Graph
   * won't alter the returned graph, and modifications to the returned
   * graph won't alter this graph.<br><br>
   * However, the graph is shallowly copied - V and E instances are not
   * copied - they will be contained in both this graph and g.
   * If deep copy behavior is necessary for V or E, extend graph and overwrite
   * this method.
   */
  public Graph<V, E> clone() {
    return new Graph<V, E>(this);
  }

  /**
   * Returns a new Graph that is an unmodifiable view of this.
   */
  public Graph<V, E> unmodifiableGraph() {
    if (unmodifiableGraph == null) {
      unmodifiableGraph = new UnmodifiableGraph(this);
    }
    return unmodifiableGraph;
  }

  /**
   * Two graphs are equivalent if they store the same vertices and edges,
   * and are the same directionality (both directed, or both undirected)
   */
  public boolean equals(Object o) {
    try {
      @SuppressWarnings("unchecked")
      Graph<V, E> g = (Graph<V, E>) o;

      return isDirected() == g.isDirected() &&
          vertexSet().equals(g.vertexSet()) &&
          edgeSet().equals(g.edgeSet());
    } catch (ClassCastException ce) {
      return false;
    }
  }

  /**
   * Hashes a Graph based on its vertices, edges, and directed-ness
   */
  public int hashCode() {
    return Objects.hash(directed, vertices, edges);
  }

  /**
   * Returns a Set of the vertices in this graph. This set is a copy of the
   * underlying keyset, so it is pass by value. Altering the returned
   * set will not affect the graph.
   */
  public Set<V> vertexSet() {
    return new HashSet<V>(vertices.keySet());
  }

  /**
   * Returns true iff this graph contains the given vertex.
   */
  public boolean containsVertex(V v) {
    return vertices.containsKey(v);
  }

  /**
   * Returns the number of vertices in this graph
   */
  public int vertexSize() {
    return vertices.size();
  }

  /**
   * Returns the vertex object tied to the given vertex key.
   * Returns null if v is not contained in this graph
   */
  protected Vertex getVertex(V v) {
    return vertices.get(v);
  }

  /**
   * Returns a Set of the edges in this graph. This set is a copy of the underlying
   * keyset, so it is pass by value. Altering the returned set will not affect
   * the graph.
   */
  public Set<E> edgeSet() {
    return new HashSet<E>(edges.keySet());
  }

  /**
   * Returns true iff this graph contains the edge E
   */
  public boolean containsEdge(E e) {
    return edges.containsKey(e);
  }

  /**
   * Returns the number of edges in this graph
   */
  public int edgeSize() {
    return edges.size();
  }

  /**
   * Returns the edge object tied to the given edge key.
   * Returns null if e is not contained in this graph
   */
  protected Edge getEdge(E e) {
    return edges.get(e);
  }

  /**
   * Returns whether or not this graph is directed.
   * If this graph is undirected, all edges are treated as bi-directional
   * for the purposes of directed-graph algorithms.
   */
  public boolean isDirected() {
    return directed;
  }

  /**
   * Adds the given vertex to the graph with no edges.
   * If the vertex is already in the graph, do nothing and return false
   * Returns true if the an operation is performed this way, false otw.
   */
  public boolean addVertex(V v) {
    if (vertices.containsKey(v))
      return false;

    vertices.put(v, new Vertex(v));
    return true;
  }

  /**
   * Connects the two given vertices in the graph by adding an edge.
   * If there is already a connection from source to sink
   * (or sink to source and this is undirected), or e is already
   * an edge in this graph, do nothing and return false.
   * Returns true if an operation is performed this way, false otw.
   *
   * @throws NotInCollectionException - if source or sink are not vertices in this graph
   */
  public boolean addEdge(V source, V sink, E e) throws NotInCollectionException {
    if (!vertices.containsKey(source) || !vertices.containsKey(sink))
      throw new NotInCollectionException("Can't create edge " + e, source, sink);
    if (edges.containsKey(e))
      return false;
    if (isConnected(source, sink))
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

  /**
   * Removes this vertices (And all of the edges going in and out of it) from the graph.
   * Returns true if the vertex was removed this way, false if there was no vertex to remove.
   */
  public boolean removeVertex(V v) {
    if (!vertices.containsKey(v))
      return false;

    Vertex vertex = vertices.get(v);
    //Remove all connections to/from v
    for (E e : vertex.inEdges.keySet())
      removeEdge(e);
    for (E e : vertex.outEdges.keySet())
      removeEdge(e);

    //Actually remove the vertex
    vertices.remove(v);
    return true;
  }

  /**
   * Removes this edge from the graph.
   * Returns true if the edge was removed this way, false if there was no edge to remove.
   */
  public boolean removeEdge(E e) {
    if (!edges.containsKey(e))
      return false;

    Edge edge = edges.get(e);
    Vertex source = edge.getSource();
    Vertex sink = edge.getSink();

    source.outEdges.remove(e);
    sink.inEdges.remove(e);
    edges.remove(e);
    return true;
  }

  /**
   * Clears this graph entirely, removing all edges and vertices.
   */
  public void clear() {
    vertices.clear();
    edges.clear();
  }

  /**
   * Returns the edge with the given source and sink, if any. Returns null
   * if there is no such edge.
   * For undirected graphs, returns any edge that connects the two, in either direction
   *
   * @throws NotInCollectionException - if source or sink are not vertices in this graph
   */
  public E getConnection(V source, V sink) throws NotInCollectionException {
    if (!vertices.containsKey(source) || !vertices.containsKey(sink))
      throw new NotInCollectionException("Can't check for connection", source, sink);

    Vertex sourceV = vertices.get(source);
    Vertex sinkV = vertices.get(sink);
    for (E e : sourceV.outEdges.keySet()) {
      if (sinkV.inEdges.containsKey(e)) {
        return e;
      }
    }
    if (!directed) {
      for (E e : sourceV.inEdges.keySet()) {
        if (sinkV.outEdges.containsKey(e)) {
          return e;
        }
      }
    }
    return null;
  }

  /**
   * Returns true iff there is an edge with the given source and sink.
   * For undirected graphs, returns true iff there is any edge that connects the
   * two, in either direction.
   *
   * @throws NotInCollectionException - if source or sink are not vertices in this graph
   */
  public boolean isConnected(V source, V sink) throws NotInCollectionException {
    return getConnection(source, sink) != null;
  }

  /**
   * Returns the vertex at the other end of the given edge.
   * Returns null if oneEnd is neither end of e.
   *
   * @throws NotInCollectionException if e isn't an edge in this graph
   */
  public V getOther(E e, V oneEnd) throws NotInCollectionException {
    if (!edges.containsKey(e))
      throw new NotInCollectionException("Can't find other endpoint of edge", e);
    Edge edge = edges.get(e);

    if (edge._1.v.equals(oneEnd)) return edge._3.v;
    if (edge._3.v.equals(oneEnd)) return edge._1.v;
    return null;
  }

  /**
   * Returns a set of all edges with v as an endpoint (source or sink)
   *
   * @throws NotInCollectionException if v is not in this graph
   */
  public Set<E> edgeSetOf(V v) throws NotInCollectionException {
    if (!vertices.containsKey(v))
      throw new NotInCollectionException("Can't get source and sink set", v);
    HashSet<E> e = new HashSet<E>(vertices.get(v).outEdges.keySet());
    e.addAll(vertices.get(v).inEdges.keySet());
    return e;
  }

  /**
   * Returns the number of edges with the given vertex as an endpoint (source or sink)
   *
   * @throws NotInCollectionException if v is not in this graph
   */
  public int degreeOf(V v) throws NotInCollectionException {
    if (!vertices.containsKey(v))
      throw new NotInCollectionException("Can't get degree", v);
    return vertices.get(v).inEdges.size() + vertices.get(v).outEdges.size();
  }

  /**
   * Returns a set of all edges with v as a source.
   * If this graph is undirected returns edgeSetOf(source) instead, as
   * every vertex is source and sink
   *
   * @throws NotInCollectionException if source is not in this graph
   */
  public Set<E> edgeSetOfSource(V source) throws NotInCollectionException {
    if (!vertices.containsKey(source))
      throw new NotInCollectionException("Can't get source set", source);
    if (directed)
      return new HashSet<E>(vertices.get(source).outEdges.keySet());
    else
      return edgeSetOf(source);
  }

  /**
   * Returns the number of edges with the given vertex as an source.
   * If this graph is undirected, returns degreeOf(source) instead, as
   * every vertex is a source and sink.
   *
   * @throws NotInCollectionException if v is not in this graph
   */
  public int outDegreeOf(V source) throws NotInCollectionException {
    if (!vertices.containsKey(source))
      throw new NotInCollectionException("Can't get degree", source);
    if (directed)
      return vertices.get(source).outEdges.size();
    else
      return degreeOf(source);
  }

  /**
   * Returns a set of all edges with v as a sink.
   * If this graph is undirected returns edgeSetOf(source) instead, as
   * every vertex is source and sink
   *
   * @throws NotInCollectionException if sink is not in this graph
   */
  public Set<E> edgeSetOfSink(V sink) throws NotInCollectionException {
    if (!vertices.containsKey(sink))
      throw new NotInCollectionException("Can't get sink set", sink);
    if (directed)
      return new HashSet<E>(vertices.get(sink).inEdges.keySet());
    else
      return edgeSetOf(sink);
  }

  /**
   * Returns the number of edges with the given vertex as an sink.
   * If this graph is undirected, returns degreeOf(sink) instead, as
   * every vertex is a source and sink.
   *
   * @throws NotInCollectionException if v is not in this graph
   */
  public int inDegreeOf(V sink) throws NotInCollectionException {
    if (!vertices.containsKey(sink))
      throw new NotInCollectionException("Can't get degree", sink);
    if (directed)
      return vertices.get(sink).inEdges.size();
    else
      return degreeOf(sink);
  }

  /**
   * Returns the source of the given edge. If this is undirected, returns the
   * first endpoint - the endpoint given first when the edge was created.
   *
   * @throws NotInCollectionException if e is not in this graph
   */
  public V sourceOf(E e) throws NotInCollectionException {
    if (!edges.containsKey(e))
      throw new NotInCollectionException("Can't get source of", e);
    return edges.get(e).getSource().v;
  }

  /**
   * Returns the sink of the given edge. If this is undirected, returns a
   * second endpoint - the endpoint given second when the edge was created.
   *
   * @throws NotInCollectionException if e is not in this graph
   */
  public V sinkOf(E e) throws NotInCollectionException {
    if (!edges.containsKey(e))
      throw new NotInCollectionException("Can't get source of", e);
    return edges.get(e).getSink().v;
  }

  /**
   * Returns the vertices on either end of the given edge - a List of length 2
   * The returned list is read-only - attempts to modify it will cause an
   * UnsupportedOperationException.
   *
   * @throws NotInCollectionException if e is not in this graph
   */
  public Tuple2<V, V> verticesOf(E e) throws NotInCollectionException {
    if (!edges.containsKey(e))
      throw new NotInCollectionException("Can't get verticies of", e);
    Edge edge = edges.get(e);
    return Tuple.of(edge.getSource().v, edge.getSink().v);
  }

  /**
   * Returns true if the given edge is a self edge - two endpoints are the same
   * vertex, false otherwise.
   *
   * @throws NotInCollectionException if e is not in this graph
   */
  public boolean isSelfEdge(E e) throws NotInCollectionException {
    if (!edges.containsKey(e))
      throw new NotInCollectionException("Can't determine if is self edge", e);
    return sourceOf(e).equals(sinkOf(e));
  }

  /**
   * Returns true iff v is one endpoint of e, false otherwise
   *
   * @throws NotInCollectionException if e is not in this graph
   */
  public boolean isEndpointOf(E e, V endpoint) throws NotInCollectionException {
    if (!edges.containsKey(e))
      throw new NotInCollectionException("Can't determine if isEndpoint", e);
    return endpoint.equals(sourceOf(e)) || endpoint.equals(sinkOf(e));
  }

  /**
   * Returns the shared endpoint between the two edges. If both endpoints are
   * shared, an arbitrary endpoint is returned. If they do not share an endpoint,
   * returns null
   */
  public V getSharedEndpoint(E e1, E e2) throws NotInCollectionException {
    if (!edges.containsKey(e1) || !edges.containsKey(e2))
      throw new NotInCollectionException("Can't getSharedEndpoint", e1, e2);

    V v11 = sourceOf(e1);
    V v12 = sinkOf(e1);
    V v21 = sourceOf(e2);
    V v22 = sinkOf(e2);

    if (v11.equals(v21)) return v11;
    if (v11.equals(v22)) return v11;
    if (v12.equals(v21)) return v12;
    if (v12.equals(v22)) return v22;

    return null;
  }

  /**
   * Returns all neighbor vertices to {@code v}. In a directed graph
   * this is the set of vertices {@code a in A} for which there exists an edge e
   * with v as the source and a as the sink. In an undirected graph,
   * this is the set of vertices for which there exists an edge e with v as
   * either the source or the sink and a as the other endpoint.
   */
  public Set<V> neighborsOf(V v) throws NotInCollectionException {
    if (!vertices.containsKey(v))
      throw new NotInCollectionException("Can't get neighbor set", v);

    Vertex vertex = vertices.get(v);
    HashSet<V> neighbors = new HashSet<>();
    for (Edge e : vertex.outEdges.values()) {
      neighbors.add(e.getSink().v);
    }
    if (!directed) {
      for (Edge e : vertex.inEdges.values()) {
        neighbors.add(e.getSource().v);
      }
    }
    return neighbors;
  }

  /**
   * Returns all vertices that are shared neighbors of v1 and v2.
   */
  public Set<V> sharedNeighborsOf(V v1, V v2) throws NotInCollectionException {
    Set<V> neighbors = neighborsOf(v1);
    neighbors.retainAll(neighborsOf(v2));
    return neighbors;
  }

}
