package graph;

import static org.junit.Assert.*;
import graph.Graph.NotInGraphException;

import org.junit.Test;

public class GraphTest {

	@Test
	public void testAddition() {
		Graph<String, Integer> g = new Graph<>();
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
}
