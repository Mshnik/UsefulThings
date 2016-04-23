package graph;

import common.IDObject;

/**
 * @author Mshnik
 * //TODO - SPEC
 */
public class CapacityLimitedVertex extends IDObject implements Flowable, Cloneable {

  private String name;

  private SimpleVertex inVertex;
  private SimpleEdge edge;
  private SimpleVertex outVertex;


  public CapacityLimitedVertex(int capacity) {
    inVertex = new SimpleVertex();
    outVertex = new SimpleVertex();
    edge = new SimpleEdge(0, capacity);
  }

  public SimpleVertex getInVertex() {
    return inVertex;
  }

  public SimpleVertex getOutVertex() {
    return outVertex;
  }

  public SimpleEdge getNestedEdge() {
    return edge;
  }

  @Override
  public CapacityLimitedVertex clone() {
    return new CapacityLimitedVertex(getCapacity());
  }

  @Override
  public int getCapacity() {
    return edge.getCapacity();
  }

  /**
   * Returns the name of this CapacityLimitedVertex. Not used for any calculation purposes
   * (only for display and toString), but cannot be null.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this CapacityLimitedVertex. Not used for any calculation purposes
   * (only for display and toString), but cannot be null.
   */
  public void setName(String n) {
    if (n == null) throw new IllegalArgumentException("Can't set name to null");
    name = n;
    inVertex.setName(n + " - in");
    outVertex.setName(n + " - out");
    edge.setName(n + " - edge");
  }
}
