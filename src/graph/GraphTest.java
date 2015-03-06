package graph;

import static org.junit.Assert.*;
import graph.Graph.NotInGraphException;

import org.junit.Test;

public class GraphTest {

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
	public void testRemoval(){
		Graph<String, Integer> g = new Graph<>();
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addEdge("A", "B", 1);
		g.addEdge("A", "C", 2);
		g.addEdge("B", "C", 3);
		
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
		Graph<String, Integer> g = new Graph<>();
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addEdge("A", "B", 1);
		g.addEdge("A", "C", 2);
		g.addEdge("B", "C", 3);
		
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
		
		changed = g.addEdge("B", "C", 1);
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
		Graph<String, Integer> g = new Graph<>();
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addEdge("A", "B", 1);
		g.addEdge("A", "C", 2);
		g.addEdge("C","C",3);
		
		assertEquals(new Integer(1), g.getConnection("A", "B"));
		assertEquals(new Integer(2), g.getConnection("A", "C"));
		assertEquals(new Integer(3), g.getConnection("C", "C"));
		
		try{
			g.getConnection("A", "F");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}
		
		try{
			g.getConnection("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}
		
		assertTrue(g.isConnected("A", "B"));
		assertFalse(g.isConnected("B", "A"));
		assertFalse(g.isConnected("B", "C"));
		assertFalse(g.isConnected("A", "A"));
		assertTrue(g.isConnected("C", "C"));
		
		try{
			g.isConnected("A", "F");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}
		try{
			g.isConnected("F", "A");
			fail("Got connection to vertex not in graph");
		}catch(NotInGraphException e){}
		
		assertEquals("B", g.getOther(1, "A"));
		assertEquals("A", g.getOther(1, "B"));
		assertEquals("A", g.getOther(2, "C"));
		assertEquals("C", g.getOther(3, "C"));
		assertEquals(null, g.getOther(1, "C"));
		
		try{
			g.getOther(15, "A");
			fail("Got other endpoint of edge not in graph");
		}catch(NotInGraphException e){}
		
	}
}
