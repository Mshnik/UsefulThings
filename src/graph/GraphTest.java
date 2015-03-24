package graph;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import common.dataStructures.NotInCollectionException;

import org.junit.Before;
import org.junit.Test;

public class GraphTest {

	private Graph<String, Integer> g; //Directed graph
	private Graph<String, Integer> gU; //Undirected graph

	@Before
	public void setup(){
		g = new Graph<>();
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addEdge("A", "B", 1);
		g.addEdge("A", "C", 2);
		g.addEdge("C","C",3);

		gU = new Graph<>(false);
		gU.addVertex("A");
		gU.addVertex("B");
		gU.addVertex("C");
		gU.addEdge("A", "B", 1);
		gU.addEdge("A", "C", 2);
		gU.addEdge("C","C",3);
	}

	@Test
	public void testAddition() {
		Graph<String, Integer> g = new Graph<>();
		assertTrue(g.isDirected());
		g.addVertex("A");

		assertEquals(1, g.vertexSet().size());
		assertEquals(0, g.edgeSet().size());
		assertTrue(g.vertexSet().contains("A"));

		g.addVertex("B");
		assertEquals(2, g.vertexSet().size());
		assertEquals(0, g.edgeSet().size());
		assertTrue(g.vertexSet().contains("A"));
		assertTrue(g.vertexSet().contains("B"));

		g.addVertex("C");
		assertEquals(3, g.vertexSet().size());
		assertEquals(0, g.edgeSet().size());
		assertTrue(g.vertexSet().contains("A"));
		assertTrue(g.vertexSet().contains("B"));
		assertTrue(g.vertexSet().contains("C"));

		g.addEdge("A", "B", 1);
		assertEquals(3, g.vertexSet().size());
		assertEquals(1, g.edgeSet().size());
		assertTrue(g.edgeSet().contains(1));

		g.addEdge("B", "C", 2);
		assertEquals(3, g.vertexSet().size());
		assertEquals(2, g.edgeSet().size());
		assertTrue(g.edgeSet().contains(1));
		assertTrue(g.edgeSet().contains(2));

		g.addEdge("A","A",3);
		assertEquals(3, g.vertexSet().size());
		assertEquals(3, g.edgeSet().size());
		assertTrue(g.edgeSet().contains(1));
		assertTrue(g.edgeSet().contains(2));
		assertTrue(g.edgeSet().contains(3));
	}

	@Test
	public void testClone(){
		Graph<String, Integer> g2 = g.clone();

		assertEquals(g.vertexSet(), g2.vertexSet());
		assertEquals(g.edgeSet(), g2.edgeSet());
		assertEquals(g.isDirected(), g2.isDirected());

		g2.addVertex("Y");
		g2.addEdge("B", "Y", 4);

		assertFalse(g.vertexSet().equals(g2.vertexSet()));
		assertFalse(g.edgeSet().equals(g2.edgeSet()));

		g.addVertex("Y");
		g.addEdge("B", "Y", 4);

		assertEquals(g.vertexSet(), g2.vertexSet());
		assertEquals(g.edgeSet(), g2.edgeSet());
	}

	@Test
	public void testRemoval(){
		assertEquals(3, g.vertexSet().size());
		assertEquals(3, g.edgeSet().size());
		assertTrue(g.vertexSet().contains("A"));
		assertTrue(g.vertexSet().contains("B"));
		assertTrue(g.vertexSet().contains("C"));
		assertTrue(g.edgeSet().contains(1));
		assertTrue(g.edgeSet().contains(2));
		assertTrue(g.edgeSet().contains(3));

		boolean changed = g.removeEdge(3);

		assertEquals(3, g.vertexSet().size());
		assertEquals(2, g.edgeSet().size());
		assertTrue(g.vertexSet().contains("A"));
		assertTrue(g.vertexSet().contains("B"));
		assertTrue(g.vertexSet().contains("C"));
		assertTrue(g.edgeSet().contains(1));
		assertTrue(g.edgeSet().contains(2));
		assertFalse(g.edgeSet().contains(3));
		assertTrue(changed);

		changed = g.removeVertex("C");

		assertEquals(2, g.vertexSet().size());
		assertEquals(1, g.edgeSet().size());
		assertTrue(g.vertexSet().contains("A"));
		assertTrue(g.vertexSet().contains("B"));
		assertFalse(g.vertexSet().contains("C"));
		assertTrue(g.edgeSet().contains(1));
		assertFalse(g.edgeSet().contains(2));
		assertFalse(g.edgeSet().contains(3));
		assertTrue(changed);

		changed = g.removeVertex("A");

		assertEquals(1, g.vertexSet().size());
		assertEquals(0, g.edgeSet().size());
		assertFalse(g.vertexSet().contains("A"));
		assertTrue(g.vertexSet().contains("B"));
		assertFalse(g.vertexSet().contains("C"));
		assertFalse(g.edgeSet().contains(1));
		assertFalse(g.edgeSet().contains(2));
		assertFalse(g.edgeSet().contains(3));
		assertTrue(changed);

		changed = g.removeVertex("A");
		assertFalse(changed);
	}

	@Test
	public void testBadAddition(){
		assertEquals(3, g.vertexSize());
		assertEquals(3, g.edgeSize());
		assertTrue(g.vertexSet().contains("A"));
		assertTrue(g.vertexSet().contains("B"));
		assertTrue(g.vertexSet().contains("C"));
		assertTrue(g.edgeSet().contains(1));
		assertTrue(g.edgeSet().contains(2));
		assertTrue(g.edgeSet().contains(3));

		boolean changed = g.addVertex("A");
		assertFalse(changed);

		changed = g.addEdge("A", "B", 1);
		assertFalse(changed);

		changed = g.addEdge("A","B",12);
		assertFalse(changed);

		changed = g.addEdge("B", "C", 1);
		assertFalse(changed);

		changed = gU.addEdge("A", "B", 12);
		assertFalse(changed);

		changed = gU.addEdge("B", "A", 12);
		assertFalse(changed);

		try{
			g.addEdge("A","F", 5);
			fail("Able to add edge to non-existant node");
		}catch(NotInCollectionException e){}

		try{
			g.addEdge("F","B", 6);
			fail("Able to add edge to non-existant node");
		}catch(NotInCollectionException e){}
	}

	@Test
	public void testGetters(){
		assertEquals(new Integer(1), g.getConnection("A", "B"));
		assertEquals(new Integer(2), g.getConnection("A", "C"));
		assertEquals(new Integer(3), g.getConnection("C", "C"));

		assertEquals(new Integer(1), gU.getConnection("A", "B"));
		assertEquals(new Integer(1), gU.getConnection("B", "A"));
		assertEquals(new Integer(2), gU.getConnection("A", "C"));
		assertEquals(new Integer(2), gU.getConnection("C", "A"));
		assertEquals(new Integer(3), gU.getConnection("C", "C"));

		try{
			g.getConnection("A", "F");
			fail("Got connection to vertex not in graph");
		}catch(NotInCollectionException e){}
		try{
			gU.getConnection("A", "F");
			fail("Got connection to vertex not in graph");
		}catch(NotInCollectionException e){}

		try{
			g.getConnection("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInCollectionException e){}
		try{
			gU.getConnection("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInCollectionException e){}

		assertTrue(g.isConnected("A", "B"));
		assertFalse(g.isConnected("B", "A"));
		assertFalse(g.isConnected("B", "C"));
		assertFalse(g.isConnected("A", "A"));
		assertTrue(g.isConnected("C", "C"));

		assertTrue(gU.isConnected("A", "B"));
		assertTrue(gU.isConnected("B", "A"));
		assertFalse(gU.isConnected("B", "C"));
		assertFalse(gU.isConnected("A", "A"));
		assertTrue(gU.isConnected("C", "C"));

		try{
			g.isConnected("A", "F");
			fail("Got connection to vertex not in graph");
		}catch(NotInCollectionException e){}
		try{
			gU.isConnected("A", "F");
			fail("Got connection to vertex not in graph");
		}catch(NotInCollectionException e){}

		try{
			g.isConnected("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInCollectionException e){}
		try{
			gU.isConnected("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInCollectionException e){}

		assertEquals("B", g.getOther(1, "A"));
		assertEquals("A", g.getOther(1, "B"));
		assertEquals("A", g.getOther(2, "C"));
		assertEquals("C", g.getOther(3, "C"));
		assertEquals(null, g.getOther(1, "C"));

		assertEquals("B", gU.getOther(1, "A"));
		assertEquals("A", gU.getOther(1, "B"));
		assertEquals("A", gU.getOther(2, "C"));
		assertEquals("C", gU.getOther(3, "C"));
		assertEquals(null, gU.getOther(1, "C"));

		try{
			g.getOther(15, "A");
			fail("Got other endpoint of edge not in graph");
		}catch(NotInCollectionException e){}

		try{
			gU.getOther(15, "A");
			fail("Got other endpoint of edge not in graph");
		}catch(NotInCollectionException e){}
	}

	@Test
	public void testEdgeSetOf(){
		HashSet<Integer> iSet = new HashSet<>();

		assertEquals(iSet, g.edgeSetOfSource("B"));
		iSet.add(1);
		assertEquals(iSet, g.edgeSetOf("B"));
		assertEquals(iSet, g.edgeSetOfSink("B"));

		assertEquals(iSet, gU.edgeSetOf("B"));
		assertEquals(iSet, gU.edgeSetOfSink("B"));
		assertEquals(iSet, gU.edgeSetOfSource("B"));

		iSet.clear();

		assertEquals(iSet, g.edgeSetOfSink("A"));
		iSet.add(1);
		iSet.add(2);
		assertEquals(iSet, g.edgeSetOf("A"));
		assertEquals(iSet, g.edgeSetOfSource("A"));

		assertEquals(iSet, gU.edgeSetOf("A"));
		assertEquals(iSet, gU.edgeSetOfSink("A"));
		assertEquals(iSet, gU.edgeSetOfSource("A"));

		iSet.clear();

		iSet.add(3);
		assertEquals(iSet, g.edgeSetOfSource("C"));
		iSet.add(2);
		assertEquals(iSet, g.edgeSetOf("C"));
		assertEquals(iSet, g.edgeSetOfSink("C"));

		assertEquals(iSet, gU.edgeSetOf("C"));
		assertEquals(iSet, gU.edgeSetOfSink("C"));
		assertEquals(iSet, gU.edgeSetOfSource("C"));

		try{
			g.edgeSetOf("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInCollectionException e){}

		try{
			g.edgeSetOfSink("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInCollectionException e){}

		try{
			g.edgeSetOfSource("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInCollectionException e){}

		try{
			gU.edgeSetOf("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInCollectionException e){}

		try{
			gU.edgeSetOfSink("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInCollectionException e){}

		try{
			gU.edgeSetOfSource("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInCollectionException e){}

		assertTrue(g.isEndpointOf(1, "A"));
		assertTrue(g.isEndpointOf(1, "B"));
		assertFalse(g.isEndpointOf(1, "C"));
		assertTrue(g.isEndpointOf(2, "A"));
		assertFalse(g.isEndpointOf(2, "B"));
		assertTrue(g.isEndpointOf(2, "C"));
		assertFalse(g.isEndpointOf(3, "A"));
		assertFalse(g.isEndpointOf(3, "B"));
		assertTrue(g.isEndpointOf(3, "C"));
		assertFalse(g.isEndpointOf(2, "ZZ"));

		try{
			g.isEndpointOf(12, "A");
			fail("Checked endpoint of non-existent edge");
		}catch(NotInCollectionException e){}

		assertTrue(gU.isEndpointOf(1, "A"));
		assertTrue(gU.isEndpointOf(1, "B"));
		assertFalse(gU.isEndpointOf(1, "C"));
		assertTrue(gU.isEndpointOf(2, "A"));
		assertFalse(gU.isEndpointOf(2, "B"));
		assertTrue(gU.isEndpointOf(2, "C"));
		assertFalse(gU.isEndpointOf(3, "A"));
		assertFalse(gU.isEndpointOf(3, "B"));
		assertTrue(gU.isEndpointOf(3, "C"));
		assertFalse(gU.isEndpointOf(2, "ZZ"));

		try{
			gU.isEndpointOf(12, "A");
			fail("Checked endpoint of non-existent edge");
		}catch(NotInCollectionException e){}

		assertEquals("A", g.getSharedEndpoint(1, 2));
		assertEquals("C", g.getSharedEndpoint(2, 3));
		assertEquals(null, g.getSharedEndpoint(1, 3));

		try{
			g.getSharedEndpoint(12, 2);
			fail("Found shared endpoint of non-existant edge");
		}catch(NotInCollectionException e){}

		assertEquals("A", gU.getSharedEndpoint(1, 2));
		assertEquals("C", gU.getSharedEndpoint(2, 3));
		assertEquals(null, gU.getSharedEndpoint(1, 3));

		try{
			gU.getSharedEndpoint(12, 2);
			fail("Found shared endpoint of non-existant edge");
		}catch(NotInCollectionException e){}

	}

	@Test
	public void testVertexSet(){
		ArrayList<String> lst = new ArrayList<>();
		lst.add("A");
		lst.add("B");
		assertEquals(lst, g.verticesOf(1));
		assertEquals(lst, gU.verticesOf(1));

		lst.clear();
		lst.add("A");
		lst.add("C");
		assertEquals(lst, g.verticesOf(2));
		assertEquals(lst, gU.verticesOf(2));

		lst.clear();
		lst.add("C");
		lst.add("C");
		assertEquals(lst, g.verticesOf(3));
		assertEquals(lst, gU.verticesOf(3));
	}

	@Test
	public void testNeighbors(){
		HashSet<String> neighbors = new HashSet<>();

		assertEquals(neighbors, g.neighborsOf("B"));

		neighbors.add("C");
		assertEquals(neighbors, g.neighborsOf("C"));

		neighbors.add("B");
		assertEquals(neighbors, g.neighborsOf("A"));

		assertEquals(neighbors, gU.neighborsOf("A"));

		neighbors.clear();
		neighbors.add("C");
		neighbors.add("A");
		assertEquals(neighbors, gU.neighborsOf("C"));

		neighbors.clear();
		neighbors.add("A");
		assertEquals(neighbors, gU.neighborsOf("B"));
	}

	@Test
	public void testEqualityAndHash(){
		Graph<String, Integer> g2 = g.clone();

		assertTrue(g.equals(g2));
		assertTrue(g2.equals(g));
		assertEquals(g.hashCode(), g2.hashCode());

		assertFalse(gU.equals(g));
		assertFalse(g.equals(gU));

		g.addVertex("X");
		assertFalse(g.equals(g2));
		assertFalse(g2.equals(g));

		g2.addVertex("X");
		assertTrue(g.equals(g2));
		assertTrue(g2.equals(g));
		assertEquals(g.hashCode(), g2.hashCode());

		g.addEdge("B","C",7);
		assertFalse(g.equals(g2));
		assertFalse(g2.equals(g));

		g2.addEdge("B","C",8);
		assertFalse(g.equals(g2));
		assertFalse(g2.equals(g));

		g2.removeEdge(8);
		g2.addEdge("B","C",7);
		assertTrue(g.equals(g2));
		assertTrue(g2.equals(g));
		assertEquals(g.hashCode(), g2.hashCode());
	}

	@Test
	public void testUnmodifiableGraph(){
		Graph<String, Integer> unmodifiableG = g.unmodifiableGraph();

		assertEquals(g.isDirected(), unmodifiableG.isDirected());
		assertEquals(g.vertexSet(), unmodifiableG.vertexSet());
		assertEquals(g.edgeSet(), unmodifiableG.edgeSet());

		assertEquals(g.vertexSize(), unmodifiableG.vertexSize());
		assertEquals(g.edgeSize(), unmodifiableG.edgeSize());

		assertTrue(g.equals(unmodifiableG));
		assertTrue(unmodifiableG.equals(g));

		assertEquals(g.hashCode(), unmodifiableG.hashCode());

		for(int i = 60; i < 70; i++){ //Char codes with caps letters in the middle
			String s = ((char)i) + "";
			assertEquals(g.containsVertex(s), unmodifiableG.containsVertex(s));
		}

		for(int i = 0; i < 10; i++){
			assertEquals(g.containsEdge(i), unmodifiableG.containsEdge(i));
		}

		for(String v1 : g.vertexSet()){
			assertEquals(g.degreeOf(v1), unmodifiableG.degreeOf(v1));
			assertEquals(g.edgeSetOf(v1), unmodifiableG.edgeSetOf(v1));
			assertEquals(g.inDegreeOf(v1), unmodifiableG.inDegreeOf(v1));
			assertEquals(g.edgeSetOfSink(v1), unmodifiableG.edgeSetOfSink(v1));
			assertEquals(g.outDegreeOf(v1), unmodifiableG.outDegreeOf(v1));
			assertEquals(g.edgeSetOfSource(v1), unmodifiableG.edgeSetOfSource(v1));
			assertEquals(g.neighborsOf(v1), unmodifiableG.neighborsOf(v1));

			for(String v2 : g.vertexSet()){
				assertEquals(g.getConnection(v1, v2), unmodifiableG.getConnection(v1, v2));
				assertEquals(g.isConnected(v1, v2), unmodifiableG.isConnected(v1, v2));
			}

			for(Integer e : g.edgeSet()){
				assertEquals(g.getOther(e, v1), unmodifiableG.getOther(e, v1));
				assertEquals(g.isEndpointOf(e, v1), unmodifiableG.isEndpointOf(e, v1));
			}
		}

		for(Integer e : g.edgeSet()){
			assertEquals(g.sourceOf(e), unmodifiableG.sourceOf(e));
			assertEquals(g.sinkOf(e), unmodifiableG.sinkOf(e));
			assertEquals(g.verticesOf(e), unmodifiableG.verticesOf(e));
			assertEquals(g.isSelfEdge(e), unmodifiableG.isSelfEdge(e));

			for(Integer e2 : g.edgeSet()){
				assertEquals(g.getSharedEndpoint(e, e2), unmodifiableG.getSharedEndpoint(e, e2));
			}
		}

		assertTrue(unmodifiableG == unmodifiableG.unmodifiableGraph());

		try{
			unmodifiableG.addVertex("ASDF");
			fail("Modified UnmodifiableGraph");
		}catch(UnsupportedOperationException e){}

		try{
			unmodifiableG.addEdge("C", "C", 15);
			fail("Modified UnmodifiableGraph");
		}catch(UnsupportedOperationException e){}

		try{
			unmodifiableG.removeVertex("B");
			fail("Modified UnmodifiableGraph");
		}catch(UnsupportedOperationException e){}

		try{
			unmodifiableG.removeEdge(1);
			fail("Modified UnmodifiableGraph");
		}catch(UnsupportedOperationException e){}

		try{
			unmodifiableG.clear();
			fail("Modified UnmodifiableGraph");
		}catch(UnsupportedOperationException e){}

		//Test adding a vertex is reflected in view
		g.addVertex("Z");

		assertEquals(g.vertexSet(), unmodifiableG.vertexSet());

	}

	@Test
	public void testIsDAG(){
		//Check that undirected graph is false
		assertFalse(Algorithm.isDAG(gU));

		//Check actual directed graph
		Graph<String, Integer> g = new Graph<String, Integer>();
		assertTrue(Algorithm.isDAG(g));

		g.addVertex("A");
		g.addVertex("B");
		g.addEdge("A", "B", 1);
		assertTrue(Algorithm.isDAG(g));

		g.addVertex("C");
		g.addEdge("B","C",2);
		assertTrue(Algorithm.isDAG(g));

		g.addEdge("C","A",3);
		assertFalse(Algorithm.isDAG(g));

		g.removeEdge(3);
		assertTrue(Algorithm.isDAG(g));

		g.addEdge("B","A",3);
		assertFalse(Algorithm.isDAG(g));

		g.removeEdge(3);
		assertTrue(Algorithm.isDAG(g));

		g.addEdge("A","A",3);
		assertFalse(Algorithm.isDAG(g));
	}

	@Test
	public void testIsBipartite(){
		Graph<String, Integer> g = new Graph<String, Integer>();
		assertTrue(Algorithm.isBipartite(g));

		g.addVertex("A");
		g.addVertex("B");
		g.addEdge("A", "B", 1);
		assertTrue(Algorithm.isBipartite(g));

		g.addVertex("C");
		g.addEdge("B","C",2);
		assertTrue(Algorithm.isBipartite(g));

		g.addEdge("C","A",3);
		assertFalse(Algorithm.isBipartite(g));

		g.removeEdge(3);
		g.addVertex("D");
		g.addVertex("E");
		g.addEdge("D","E",3);
		assertTrue(Algorithm.isBipartite(g));

		g.addEdge("C","D",4);
		assertTrue(Algorithm.isBipartite(g));

		g.addEdge("D","A",5);
		assertTrue(Algorithm.isBipartite(g));

		g.removeEdge(5);
		g.addEdge("E","A",5);
		assertFalse(Algorithm.isBipartite(g));
	}

	private static class WeightedEdge implements Weighted{
		private final int w;

		private WeightedEdge(int w){
			this.w = w;
		}

		public int getWeight(){
			return w;
		}
	}

	@Test
	public void testDijkstra(){
		Graph<String, WeightedEdge> g = new Graph<>();
		LinkedList<String> path = new LinkedList<String>();
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");

		try{
			Algorithm.dijkstra(g, "X", "A");
			fail("Ran dijkstra's on non-existent start node");
		}catch(NotInCollectionException e){}

		try{
			Algorithm.dijkstra(g, "A", "X");
			fail("Ran dijkstra's on non-existent goal node");
		}catch(NotInCollectionException e){}

		g.addEdge("A", "B", new WeightedEdge(-1));

		try{
			Algorithm.dijkstra(g, "A", "B");
			fail("Ran dijkstra's algorithm on a graph with a negative weight");
		}catch(Exception e){}

		g.removeEdge(g.getConnection("A", "B"));

		g.addEdge("A", "B", new WeightedEdge(3));
		path.clear();

		path.add("A");

		assertEquals(path, Algorithm.dijkstra(g, "A", "A"));

		path.add("B");

		assertEquals(path, Algorithm.dijkstra(g, "A", "B"));
		assertEquals(null, Algorithm.dijkstra(g, "B", "A"));
		assertEquals(null, Algorithm.dijkstra(g, "A", "C"));

		g.addEdge("A", "C", new WeightedEdge(1));
		assertEquals(path, Algorithm.dijkstra(g, "A", "B"));
		assertEquals(null, Algorithm.dijkstra(g, "B", "A"));

		path.clear();
		path.add("A");
		path.add("C");
		assertEquals(path, Algorithm.dijkstra(g, "A", "C"));

		g.addEdge("C", "B", new WeightedEdge(1));
		path.add("B");
		assertEquals(path, Algorithm.dijkstra(g, "A", "B"));

		g.addEdge("B","A", new WeightedEdge(5));
		path.removeFirst();
		path.add("A");
		assertEquals(path, Algorithm.dijkstra(g, "C", "A"));
	}

	private static class FlowEdge implements Flowable{
		private final int capacity;
		private final String name;
		private FlowEdge(String n, int c){
			name = n;
			capacity = c;
		}
		@Override
		public int getCapacity() {
			return capacity;
		}
		@Override
		public String toString(){
			return name;
		}
	}

	@Test
	public void testMaxflow(){
		Graph<String, FlowEdge> g = new Graph<>();
		g.addVertex("SOURCE");
		g.addVertex("SINK");
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");

		g.addEdge("SOURCE", "A", new FlowEdge("a",10));
		g.addEdge("SOURCE", "B", new FlowEdge("b",5));
		g.addEdge("SOURCE", "C", new FlowEdge("c",10));

		g.addEdge("A", "SINK", new FlowEdge("a2",10));
		g.addEdge("B", "SINK", new FlowEdge("b2",10));
		g.addEdge("C", "SINK", new FlowEdge("c2",5));

		assertEquals(new Integer(20), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

		g.addEdge("SOURCE", "SINK", new FlowEdge("direct", 30));
		assertEquals(new Integer(50), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

		g.addEdge("C","B", new FlowEdge("cb", 5));
		assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

		g.addEdge("B", "C", new FlowEdge("bc", 200));
		assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

		g.addVertex("D");
		g.addEdge("SOURCE", "D", new FlowEdge("d", 200));
		assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

		g.addEdge("SINK", "A", new FlowEdge("rA", 100));
		assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

		g.addEdge("B","B", new FlowEdge("bb", 200));
		assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

		g.addEdge("SINK", "SINK", new FlowEdge("sink2", 200));
		g.addEdge("SOURCE", "SOURCE", new FlowEdge("source2", 200));

		assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

		//Test on undirected graph - should allow backwards edges to be used correcly
		Graph<String, FlowEdge> g2 = new Graph<>(false);
		g2.addVertex("SOURCE");
		g2.addVertex("SINK");
		g2.addVertex("A");
		g2.addVertex("B");
		g2.addVertex("C");

		g2.addEdge("SOURCE", "A", new FlowEdge("a",10));
		g2.addEdge("SOURCE", "B", new FlowEdge("b",5));
		g2.addEdge("SOURCE", "C", new FlowEdge("c",10));

		g2.addEdge("A", "SINK", new FlowEdge("a2",10));
		g2.addEdge("B", "SINK", new FlowEdge("b2",10));
		g2.addEdge("C", "SINK", new FlowEdge("c2",5));

		assertEquals(new Integer(20), Algorithm.maxFlow(g2, "SOURCE", "SINK")._1);

		//Use an edge backwards
		g2.addEdge("B", "C", new FlowEdge("bc", 5));
		assertEquals(new Integer(25), Algorithm.maxFlow(g2, "SOURCE", "SINK")._1);

		//Use an edge backwards
		g2.addEdge("SINK", "SOURCE", new FlowEdge("directReverse", 75));
		assertEquals(new Integer(100), Algorithm.maxFlow(g2, "SOURCE", "SINK")._1);

		//Test on unconnected graph
		Graph<String, FlowEdge> g3 = new Graph<>();
		g3.addVertex("SOURCE");
		g3.addVertex("SINK");

		assertEquals(new Integer(0), Algorithm.maxFlow(g3, "SOURCE", "SINK")._1);

		g3.addEdge("SINK", "SOURCE", new FlowEdge("directReverse", 100));
		assertEquals(new Integer(0), Algorithm.maxFlow(g3, "SOURCE", "SINK")._1);
	}

	private <V,E> void testIsValidCycle(Graph<V,E> g, List<E> path){
		for(int i = 0; i < path.size() - 1; i++){
			if(g.isDirected()){
				assertEquals(g.sinkOf(path.get(i)), g.sourceOf(path.get(i+1)));
			} else{
				assertTrue(g.getSharedEndpoint(path.get(i), path.get(i+1)) != null);
			}
		}
		//Check last edge
		if(g.isDirected()){
			assertEquals(g.sinkOf(path.get(path.size()-1)), g.sourceOf(path.get(0)));
		} else{
			assertTrue(g.getSharedEndpoint(path.get(path.size()-1), path.get(0)) != null);
		}
	}

	@Test
	public void testFindCycleDirected(){
		Graph<String, Integer> g = new Graph<>();

		assertEquals(null, Algorithm.getCycle(g));

		g.addVertex("A");
		g.addEdge("A", "A", -1);

		List<Integer> cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);

		g.addVertex("B");
		g.addEdge("A","B",1);

		cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);

		g.removeEdge(-1);
		assertEquals(null, Algorithm.getCycle(g));

		g.addEdge("B", "A", 2);
		cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);

		g.addVertex("C");
		g.addEdge("A","C",3);
		cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);

		g.addEdge("C", "B", 4);
		g.removeEdge(1);
		cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);

		//Test two cycles preset
		g.addVertex("D");
		g.addEdge("B", "D", 6);
		g.addEdge("D", "C", 7);
		for(int i = 0; i < 100; i++){ //Make sure each cycle gets checked. Hopefully.
			cycle = Algorithm.getCycle(g);
			assertTrue(cycle != null);
			testIsValidCycle(g, cycle);
		}

		//Test extra component with no cycle (to not get stuck)
		g.addVertex("X");
		g.addVertex("Y");
		g.addEdge("X", "Y", 12);		
		for(int i = 0; i < 100; i++){
			cycle = Algorithm.getCycle(g);
			assertTrue(cycle != null);
			testIsValidCycle(g, cycle);
		}

	}

	@Test
	public void testFindCycleUndirected(){
		Graph<String, Integer> g = new Graph<String,Integer>(false);

		assertEquals(null, Algorithm.getCycle(g));

		g.addVertex("A");
		g.addVertex("B");
		g.addEdge("A", "A", -1);

		List<Integer> cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);

		g.removeEdge(-1);

		g.addEdge("A", "B", 1);
		assertEquals(null, Algorithm.getCycle(g));

		g.addVertex("C");
		g.addEdge("B", "C", 2);
		assertEquals(null, Algorithm.getCycle(g));

		g.addEdge("A", "C", 3);
		cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);

		g.addVertex("X");
		g.addVertex("Y");
		g.addEdge("X", "Y", 12);
		cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);

		g.addVertex("D");
		g.addEdge("A", "D", 4);
		cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);

		g.addEdge("D", "C", 5);
		cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);
		
		g.removeEdge(2);
		cycle = Algorithm.getCycle(g);
		assertTrue(cycle != null);
		testIsValidCycle(g, cycle);
	}
}
