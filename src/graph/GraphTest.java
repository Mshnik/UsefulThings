package graph;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import graph.Graph.NotInGraphException;

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
		}catch(NotInGraphException e){}

		try{
			g.addEdge("F","B", 6);
			fail("Able to add edge to non-existant node");
		}catch(NotInGraphException e){}
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
		}catch(NotInGraphException e){}
		try{
			gU.getConnection("A", "F");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}

		try{
			g.getConnection("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}
		try{
			gU.getConnection("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}

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
		}catch(NotInGraphException e){}
		try{
			gU.isConnected("A", "F");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}

		try{
			g.isConnected("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}
		try{
			gU.isConnected("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}

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
		}catch(NotInGraphException e){}

		try{
			gU.getOther(15, "A");
			fail("Got other endpoint of edge not in graph");
		}catch(NotInGraphException e){}
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
		}catch(NotInGraphException e){}

		try{
			g.edgeSetOfSink("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInGraphException e){}

		try{
			g.edgeSetOfSource("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInGraphException e){}

		try{
			gU.edgeSetOf("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInGraphException e){}

		try{
			gU.edgeSetOfSink("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInGraphException e){}

		try{
			gU.edgeSetOfSource("F");
			fail("Got edge set of vertex not in graph");
		}catch(NotInGraphException e){}
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
		}catch(NotInGraphException e){}
		
		try{
			Algorithm.dijkstra(g, "A", "X");
			fail("Ran dijkstra's on non-existent goal node");
		}catch(NotInGraphException e){}
		
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


	}
}
