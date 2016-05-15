package graph;

import common.dataStructures.NotInCollectionException;
import functional.impl.Function2;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static common.JUnitUtil.*;
import static common.JUnitUtil.assertFalse;

/**
 * @author Mshnik
 */
public class AlgorithmTest {

  @Test
  public void testIsDAG() {
    //Check that undirected graph is false
    Graph<String, Integer> gU = new Graph<>(false);
    gU.addVertex("A");
    gU.addVertex("B");
    gU.addEdge("A","B", 1);
    assertFalse(Algorithm.isDAG(gU));

    //Check actual directed graph
    Graph<String, Integer> g = new Graph<String, Integer>();
    assertTrue(Algorithm.isDAG(g));

    g.addVertex("A");
    g.addVertex("B");
    g.addEdge("A", "B", 1);
    assertTrue(Algorithm.isDAG(g));

    g.addVertex("C");
    g.addEdge("B", "C", 2);
    assertTrue(Algorithm.isDAG(g));

    g.addEdge("C", "A", 3);
    assertFalse(Algorithm.isDAG(g));

    g.removeEdge(3);
    assertTrue(Algorithm.isDAG(g));

    g.addEdge("B", "A", 3);
    assertFalse(Algorithm.isDAG(g));

    g.removeEdge(3);
    assertTrue(Algorithm.isDAG(g));

    g.addEdge("A", "A", 3);
    assertFalse(Algorithm.isDAG(g));
  }

  @Test
  public void testIsBipartite() {
    Graph<String, Integer> g = new Graph<String, Integer>();
    assertTrue(Algorithm.isBipartite(g));

    g.addVertex("A");
    g.addVertex("B");
    g.addEdge("A", "B", 1);
    assertTrue(Algorithm.isBipartite(g));

    g.addVertex("C");
    g.addEdge("B", "C", 2);
    assertTrue(Algorithm.isBipartite(g));

    g.addEdge("C", "A", 3);
    assertFalse(Algorithm.isBipartite(g));

    g.removeEdge(3);
    g.addVertex("D");
    g.addVertex("E");
    g.addEdge("D", "E", 3);
    assertTrue(Algorithm.isBipartite(g));

    g.addEdge("C", "D", 4);
    assertTrue(Algorithm.isBipartite(g));

    g.addEdge("D", "A", 5);
    assertTrue(Algorithm.isBipartite(g));

    g.removeEdge(5);
    g.addEdge("E", "A", 5);
    assertFalse(Algorithm.isBipartite(g));
  }

  @Test
  public void testDijkstra() {
    Graph<Character, SuperEdge> g = new Graph<>();
    LinkedList<Character> path = new LinkedList<>();
    g.addVertex('A');
    g.addVertex('B');
    g.addVertex('C');

    shouldFail(Algorithm::shortestPath, NotInCollectionException.class, g, 'X', 'A');
    shouldFail(Algorithm::shortestPath, NotInCollectionException.class, g, 'A', 'X');

    g.addEdge('A', 'B', new SuperEdge("ab", -1));

    shouldFail(Algorithm::shortestPath, Exception.class, g, 'A', 'B');

    g.removeEdge(g.getConnection('A', 'B'));

    g.addEdge('A', 'B', new SuperEdge("ab", 10));
    path.clear();

    Function2<Character, Character, Integer> heuristic = (v1, v2) -> Math.abs(v2 - v1);
    path.add('A');

    assertEquals(path, Algorithm.shortestPath(g, 'A', 'A'));
    assertEquals(path, Algorithm.shortestPath(g, 'A', 'A', heuristic));


    path.add('B');

    assertEquals(path, Algorithm.shortestPath(g, 'A', 'B'));
    assertEquals(null, Algorithm.shortestPath(g, 'B', 'A'));
    assertEquals(null, Algorithm.shortestPath(g, 'A', 'C'));
    assertEquals(path, Algorithm.shortestPath(g, 'A', 'B', heuristic));
    assertEquals(null, Algorithm.shortestPath(g, 'B', 'A', heuristic));
    assertEquals(null, Algorithm.shortestPath(g, 'A', 'C', heuristic));

    g.addEdge('A', 'C', new SuperEdge("ac", 4));
    assertEquals(path, Algorithm.shortestPath(g, 'A', 'B'));
    assertEquals(null, Algorithm.shortestPath(g, 'B', 'A'));
    assertEquals(path, Algorithm.shortestPath(g, 'A', 'B', heuristic));
    assertEquals(null, Algorithm.shortestPath(g, 'B', 'A', heuristic));

    path.clear();
    path.add('A');
    path.add('C');
    assertEquals(path, Algorithm.shortestPath(g, 'A', 'C'));
    assertEquals(path, Algorithm.shortestPath(g, 'A', 'C', heuristic));

    g.addEdge('C', 'B', new SuperEdge("cb", 4));
    path.add('B');
    assertEquals(path, Algorithm.shortestPath(g, 'A', 'B'));
    assertEquals(path, Algorithm.shortestPath(g, 'A', 'B', heuristic));


    g.addEdge('B', 'A', new SuperEdge("ba", 5));
    path.removeFirst();
    path.add('A');
    assertEquals(path, Algorithm.shortestPath(g, 'C', 'A'));
    assertEquals(path, Algorithm.shortestPath(g, 'C', 'A', heuristic));
  }

  @Test
  public void testMaxflow() {
    Graph<String, SuperEdge> g = new Graph<>();
    g.addVertex("SOURCE");
    g.addVertex("SINK");
    g.addVertex("A");
    g.addVertex("B");
    g.addVertex("C");

    g.addEdge("SOURCE", "A", new SuperEdge("a", 10));
    g.addEdge("SOURCE", "B", new SuperEdge("b", 5));
    g.addEdge("SOURCE", "C", new SuperEdge("c", 10));

    g.addEdge("A", "SINK", new SuperEdge("a2", 10));
    g.addEdge("B", "SINK", new SuperEdge("b2", 10));
    g.addEdge("C", "SINK", new SuperEdge("c2", 5));

    assertEquals(new Integer(20), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

    g.addEdge("SOURCE", "SINK", new SuperEdge("direct", 30));
    assertEquals(new Integer(50), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

    g.addEdge("C", "B", new SuperEdge("cb", 5));
    assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

    g.addEdge("B", "C", new SuperEdge("bc", 200));
    assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

    g.addVertex("D");
    g.addEdge("SOURCE", "D", new SuperEdge("d", 200));
    assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

    g.addEdge("SINK", "A", new SuperEdge("rA", 100));
    assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

    g.addEdge("B", "B", new SuperEdge("bb", 200));
    assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

    g.addEdge("SINK", "SINK", new SuperEdge("sink2", 200));
    g.addEdge("SOURCE", "SOURCE", new SuperEdge("source2", 200));

    assertEquals(new Integer(55), Algorithm.maxFlow(g, "SOURCE", "SINK")._1);

    //Test on unconnected graph
    Graph<String, SuperEdge> g3 = new Graph<>();
    g3.addVertex("SOURCE");
    g3.addVertex("SINK");

    assertEquals(new Integer(0), Algorithm.maxFlow(g3, "SOURCE", "SINK")._1);

    g3.addEdge("SINK", "SOURCE", new SuperEdge("directReverse", 100));
    assertEquals(new Integer(0), Algorithm.maxFlow(g3, "SOURCE", "SINK")._1);

    //Test non-unique answers, on reduction problem
    Graph<String, SuperEdge> g4 = new Graph<>();
    g4.addVertex("SOURCE");
    g4.addVertex("SINK");

    g4.addVertex("P1");
    g4.addVertex("P2");
    g4.addVertex("P3");

    g4.addEdge("SOURCE", "P1", new SuperEdge("p1", 1));
    g4.addEdge("SOURCE", "P2", new SuperEdge("p2", 1));
    g4.addEdge("SOURCE", "P3", new SuperEdge("p3", 1));

    g4.addVertex("E1");
    g4.addVertex("E2");
    g4.addVertex("E3");

    g4.addEdge("E1", "SINK", new SuperEdge("e1", 1));
    g4.addEdge("E2", "SINK", new SuperEdge("e2", 1));
    g4.addEdge("E3", "SINK", new SuperEdge("e3", 1));

    assertEquals(new Integer(0), Algorithm.maxFlow(g4, "SOURCE", "SINK")._1);

    g4.addEdge("P1", "E1", new SuperEdge("p1-e1", 1));
    assertEquals(new Integer(1), Algorithm.maxFlow(g4, "SOURCE", "SINK")._1);

    g4.addEdge("P2",  "E1", new SuperEdge("p2-e1", 1));
    assertEquals(new Integer(1), Algorithm.maxFlow(g4, "SOURCE", "SINK")._1);

    g4.addEdge("P1", "E2", new SuperEdge("p1-e2", 1));
    assertEquals(new Integer(2), Algorithm.maxFlow(g4, "SOURCE", "SINK")._1);

    g4.addEdge("P3", "E3", new SuperEdge("p3-e3", 1));
    assertEquals(new Integer(3), Algorithm.maxFlow(g4, "SOURCE", "SINK")._1);

    g4.addEdge("P1", "E3", new SuperEdge("p1-e3", 1));
    assertEquals(new Integer(3), Algorithm.maxFlow(g4, "SOURCE", "SINK")._1);

    g4.addEdge("P3", "E1", new SuperEdge("p3-e1", 1));
    assertEquals(new Integer(3), Algorithm.maxFlow(g4, "SOURCE", "SINK")._1);

    g4.addEdge("P3", "E2", new SuperEdge("p3-e2", 1));
    assertEquals(new Integer(3), Algorithm.maxFlow(g4, "SOURCE", "SINK")._1);

  }

  private <V, E> void testIsValidCycle(Graph<V, E> g, List<E> path) {
    for (int i = 0; i < path.size() - 1; i++) {
      if (g.isDirected()) {
        assertEquals(g.sinkOf(path.get(i)), g.sourceOf(path.get(i + 1)));
      } else {
        assertTrue(g.getSharedEndpoint(path.get(i), path.get(i + 1)) != null);
      }
    }
    //Check last edge
    if (g.isDirected()) {
      assertEquals(g.sinkOf(path.get(path.size() - 1)), g.sourceOf(path.get(0)));
    } else {
      assertTrue(g.getSharedEndpoint(path.get(path.size() - 1), path.get(0)) != null);
    }
  }

  @Test
  public void testFindCycleDirected() {
    Graph<String, Integer> g = new Graph<>();

    assertEquals(null, Algorithm.getCycle(g));

    g.addVertex("A");
    g.addEdge("A", "A", -1);

    List<Integer> cycle = Algorithm.getCycle(g);
    assertTrue(cycle != null);
    testIsValidCycle(g, cycle);

    g.addVertex("B");
    g.addEdge("A", "B", 1);

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
    g.addEdge("A", "C", 3);
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
    for (int i = 0; i < 100; i++) { //Make sure each cycle gets checked. Hopefully.
      cycle = Algorithm.getCycle(g);
      assertTrue(cycle != null);
      testIsValidCycle(g, cycle);
    }

    //Test extra component with no cycle (to not get stuck)
    g.addVertex("X");
    g.addVertex("Y");
    g.addEdge("X", "Y", 12);
    for (int i = 0; i < 100; i++) {
      cycle = Algorithm.getCycle(g);
      assertTrue(cycle != null);
      testIsValidCycle(g, cycle);
    }

  }

  @Test
  public void testFindCycleUndirected() {
    Graph<String, Integer> g = new Graph<String, Integer>(false);

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