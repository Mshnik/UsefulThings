package graph;

import org.junit.Test;

import static common.JUnitUtil.*;

/**
 * @author Mshnik
 */
public class SimpleGraphTest {


  @Test
  public void testConstruction() {
    SimpleGraph g = new SimpleGraph();

    assertEquals(0, g.vertexSize());
    assertEquals(0, g.edgeSize());

    int a = g.addVertex();

    assertEquals(1, g.vertexSize());
    assertEquals(0, g.edgeSize());

    assertEquals(a, g.getVertex(a).getID());
    assertTrue(g.containsVertex(g.getVertex(a)));

    int b = g.addVertex();

    assertEquals(2, g.vertexSize());
    assertEquals(0, g.edgeSize());

    assertEquals(b, g.getVertex(b).getID());
    assertTrue(g.containsVertex(g.getVertex(b)));

    int x = g.addEdge(a, b);

    assertEquals(2, g.vertexSize());
    assertEquals(1, g.edgeSize());

    assertEquals(x, g.getEdge(x).getID());
    assertTrue(g.containsEdge(g.getEdge(x)));
    assertTrue(g.isConnected(a, b));
  }
}
