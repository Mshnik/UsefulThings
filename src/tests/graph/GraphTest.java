package graph;

import static common.JUnitUtil.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import common.dataStructures.NotInCollectionException;

import common.types.Tuple;
import functional.impl.Function2;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {

  private Graph<String, Integer> g; //Directed graph
  private Graph<String, Integer> gU; //Undirected graph

  @Before
  public void setup() {
    g = new Graph<>();
    g.addVertex("A");
    g.addVertex("B");
    g.addVertex("C");
    g.addEdge("A", "B", 1);
    g.addEdge("A", "C", 2);
    g.addEdge("C", "C", 3);

    gU = new Graph<>(false);
    gU.addVertex("A");
    gU.addVertex("B");
    gU.addVertex("C");
    gU.addEdge("A", "B", 1);
    gU.addEdge("A", "C", 2);
    gU.addEdge("C", "C", 3);
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

    g.addEdge("A", "A", 3);
    assertEquals(3, g.vertexSet().size());
    assertEquals(3, g.edgeSet().size());
    assertTrue(g.edgeSet().contains(1));
    assertTrue(g.edgeSet().contains(2));
    assertTrue(g.edgeSet().contains(3));
  }

  @Test
  public void testClone() {
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
  public void testRemoval() {
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
  public void testBadAddition() {
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

    changed = g.addEdge("A", "B", 12);
    assertFalse(changed);

    changed = g.addEdge("B", "C", 1);
    assertFalse(changed);

    changed = gU.addEdge("A", "B", 12);
    assertFalse(changed);

    changed = gU.addEdge("B", "A", 12);
    assertFalse(changed);

    shouldFail(g::addEdge, NotInCollectionException.class, "A", "F", 5);
    shouldFail(g::addEdge, NotInCollectionException.class, "F", "B", 6);
  }

  @Test
  public void testGetters() {
    assertEquals(new Integer(1), g.getConnection("A", "B"));
    assertEquals(new Integer(2), g.getConnection("A", "C"));
    assertEquals(new Integer(3), g.getConnection("C", "C"));

    assertEquals(new Integer(1), gU.getConnection("A", "B"));
    assertEquals(new Integer(1), gU.getConnection("B", "A"));
    assertEquals(new Integer(2), gU.getConnection("A", "C"));
    assertEquals(new Integer(2), gU.getConnection("C", "A"));
    assertEquals(new Integer(3), gU.getConnection("C", "C"));

    shouldFail(g::getConnection, NotInCollectionException.class, "A", "F");
    shouldFail(gU::getConnection, NotInCollectionException.class, "A", "F");
    shouldFail(g::getConnection, NotInCollectionException.class, "F", "A");
    shouldFail(gU::getConnection, NotInCollectionException.class, "F", "A");

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

    shouldFail(g::isConnected, NotInCollectionException.class, "A", "F");
    shouldFail(gU::isConnected, NotInCollectionException.class, "A", "F");
    shouldFail(g::isConnected, NotInCollectionException.class, "F", "A");
    shouldFail(gU::isConnected, NotInCollectionException.class, "F", "A");

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

    shouldFail(g::getOther, NotInCollectionException.class, 15, "A");
    shouldFail(gU::getOther, NotInCollectionException.class, 15, "A");
  }

  @Test
  public void testEdgeSetOf() {
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

    shouldFail(g::edgeSetOf,  NotInCollectionException.class, "F");
    shouldFail(g::edgeSetOfSink, NotInCollectionException.class, "F");
    shouldFail(g::edgeSetOfSource, NotInCollectionException.class, "F");
    shouldFail(gU::edgeSetOf, NotInCollectionException.class, "F");
    shouldFail(gU::edgeSetOfSink, NotInCollectionException.class, "F");
    shouldFail(gU::edgeSetOfSource, NotInCollectionException.class, "F");

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

    shouldFail(g::isEndpointOf, NotInCollectionException.class, 12, "A");

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

    shouldFail(gU::isEndpointOf, NotInCollectionException.class, 12, "A");

    assertEquals("A", g.getSharedEndpoint(1, 2));
    assertEquals("C", g.getSharedEndpoint(2, 3));
    assertEquals(null, g.getSharedEndpoint(1, 3));

    shouldFail(g::getSharedEndpoint, NotInCollectionException.class, 12, 2);

    assertEquals("A", gU.getSharedEndpoint(1, 2));
    assertEquals("C", gU.getSharedEndpoint(2, 3));
    assertEquals(null, gU.getSharedEndpoint(1, 3));

    shouldFail(gU::getSharedEndpoint, NotInCollectionException.class, 12, 2);
  }

  @Test
  public void testVertexTuple() {
    assertEquals(Tuple.of("A","B"), g.verticesOf(1));
    assertEquals(Tuple.of("A","B"), gU.verticesOf(1));

    assertEquals(Tuple.of("A","C"), g.verticesOf(2));
    assertEquals(Tuple.of("A","C"), gU.verticesOf(2));

    assertEquals(Tuple.of("C","C"), g.verticesOf(3));
    assertEquals(Tuple.of("C","C"), gU.verticesOf(3));
  }

  @Test
  public void testNeighbors() {
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

    neighbors.clear();
    neighbors.add("C");
    assertEquals(neighbors, g.sharedNeighborsOf("A", "C"));

    neighbors.clear();
    assertEquals(neighbors, g.sharedNeighborsOf("A", "B"));

  }

  @Test
  public void testEqualityAndHash() {
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

    g.addEdge("B", "C", 7);
    assertFalse(g.equals(g2));
    assertFalse(g2.equals(g));

    g2.addEdge("B", "C", 8);
    assertFalse(g.equals(g2));
    assertFalse(g2.equals(g));

    g2.removeEdge(8);
    g2.addEdge("B", "C", 7);
    assertTrue(g.equals(g2));
    assertTrue(g2.equals(g));
    assertEquals(g.hashCode(), g2.hashCode());
  }

  @Test
  public void testUnmodifiableGraph() {
    Graph<String, Integer> unmodifiableG = g.unmodifiableGraph();

    assertEquals(null, g.unmodifiableGraph().vertices);
    assertEquals(null, g.unmodifiableGraph().edges);

    assertEquals(g.isDirected(), unmodifiableG.isDirected());
    assertEquals(g.vertexSet(), unmodifiableG.vertexSet());
    assertEquals(g.edgeSet(), unmodifiableG.edgeSet());

    assertEquals(g.vertexSize(), unmodifiableG.vertexSize());
    assertEquals(g.edgeSize(), unmodifiableG.edgeSize());

    assertTrue(g.equals(unmodifiableG));
    assertTrue(unmodifiableG.equals(g));

    assertEquals(g.hashCode(), unmodifiableG.hashCode());

    for (int i = 60; i < 70; i++) { //Char codes with caps letters in the middle
      String s = ((char) i) + "";
      assertEquals(g.containsVertex(s), unmodifiableG.containsVertex(s));
    }

    for (int i = 0; i < 10; i++) {
      assertEquals(g.containsEdge(i), unmodifiableG.containsEdge(i));
    }

    for (String v1 : g.vertexSet()) {
      assertEquals(g.degreeOf(v1), unmodifiableG.degreeOf(v1));
      assertEquals(g.edgeSetOf(v1), unmodifiableG.edgeSetOf(v1));
      assertEquals(g.inDegreeOf(v1), unmodifiableG.inDegreeOf(v1));
      assertEquals(g.edgeSetOfSink(v1), unmodifiableG.edgeSetOfSink(v1));
      assertEquals(g.outDegreeOf(v1), unmodifiableG.outDegreeOf(v1));
      assertEquals(g.edgeSetOfSource(v1), unmodifiableG.edgeSetOfSource(v1));
      assertEquals(g.neighborsOf(v1), unmodifiableG.neighborsOf(v1));

      for (String v2 : g.vertexSet()) {
        assertEquals(g.getConnection(v1, v2), unmodifiableG.getConnection(v1, v2));
        assertEquals(g.isConnected(v1, v2), unmodifiableG.isConnected(v1, v2));
      }

      for (Integer e : g.edgeSet()) {
        assertEquals(g.getOther(e, v1), unmodifiableG.getOther(e, v1));
        assertEquals(g.isEndpointOf(e, v1), unmodifiableG.isEndpointOf(e, v1));
      }
    }

    for (Integer e : g.edgeSet()) {
      assertEquals(g.sourceOf(e), unmodifiableG.sourceOf(e));
      assertEquals(g.sinkOf(e), unmodifiableG.sinkOf(e));
      assertEquals(g.verticesOf(e), unmodifiableG.verticesOf(e));
      assertEquals(g.isSelfEdge(e), unmodifiableG.isSelfEdge(e));

      for (Integer e2 : g.edgeSet()) {
        assertEquals(g.getSharedEndpoint(e, e2), unmodifiableG.getSharedEndpoint(e, e2));
      }
    }

    assertTrue(unmodifiableG == unmodifiableG.unmodifiableGraph());

    shouldFail(unmodifiableG::addVertex, UnsupportedOperationException.class, "ASDF");
    shouldFail(unmodifiableG::addEdge, UnsupportedOperationException.class, "C", "C", 10);
    shouldFail(unmodifiableG::removeVertex, UnsupportedOperationException.class, "B");
    shouldFail(unmodifiableG::removeEdge, UnsupportedOperationException.class, 1);
    shouldFail(unmodifiableG::clear, UnsupportedOperationException.class);

    //Test adding a vertex is reflected in view
    g.addVertex("Z");

    assertEquals(g.vertexSet(), unmodifiableG.vertexSet());
  }

  @Test
  public void testMakeDirectedGraph() {
    Graph<String, SuperEdge> g = new Graph<>();
    g.addVertex("A");
    g.addVertex("B");
    g.addVertex("C");
    g.addVertex("D");
    g.addEdge("A", "B", new SuperEdge("ab", 1));
    g.addEdge("A", "C", new SuperEdge("ac", 2));
    g.addEdge("C", "D", new SuperEdge("cd", 3));

    //Make sure self edges are handled correctly
    g.addEdge("D", "D", new SuperEdge("dd", 4));

    assertEquals(g, Algorithm.makeDirectedGraph(g));

    g.addEdge("B", "A", new SuperEdge("ab-copy", 1));
    g.addEdge("C", "A", new SuperEdge("ac-copy", 2));
    g.addEdge("D", "C", new SuperEdge("cd-copy", 3));

    Graph<String, SuperEdge> gU = new Graph<String, SuperEdge>(false);
    gU.addVertex("A");
    gU.addVertex("B");
    gU.addVertex("C");
    gU.addVertex("D");
    gU.addEdge("A", "B", new SuperEdge("ab", 1));
    gU.addEdge("A", "C", new SuperEdge("ac", 2));
    gU.addEdge("C", "D", new SuperEdge("cd", 3));
    gU.addEdge("D", "D", new SuperEdge("dd", 4));

    Graph<String, SuperEdge> directedG = Algorithm.makeDirectedGraph(gU);
    assertEquals(g, directedG);
  }
}
