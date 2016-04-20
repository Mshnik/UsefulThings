package graph;

import common.IDObject;

/**
 * A simple vertex implementation for use in SimpleGraph.
 *
 * @author Mshnik
 */
public class SimpleVertex extends IDObject implements Cloneable {

  /**
   * The name of this SimpleVertex. Not used for any calculation purposes
   * (only for display and toString), but cannot be null.
   */
  private String name;

  /**
   * Constructs a new SimpleVertex instance.
   * The name of this SimpleVertex is set to the empty string.
   */
  public SimpleVertex() {
    super();
    name = "";
  }

  /**
   * Constructs a new SimpleVertex instance with the given name and id. Only used
   * in cloning an existing SimpleVertex instance.
   */
  private SimpleVertex(String name, int id) {
    super(id);
    this.name = name;
  }

  /**
   * Returns a new SimpleVertex with the same name and ID as this vertex.
   */
  public SimpleVertex clone() {
    return new SimpleVertex(name, getID());
  }

  /**
   * Returns the name of this SimpleVertex. Not used for any calculation purposes
   * (only for display and toString), but cannot be null.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this SimpleVertex. Not used for any calculation purposes
   * (only for display and toString), but cannot be null.
   */
  public void setName(String n) {
    if (n == null) throw new IllegalArgumentException("Can't set name to null");
    name = n;
  }

  /**
   * Returns a string representation of this SimpleVertex, consisting of
   * name and ID
   */
  public String toString() {
    return name + "(" + getID() + ")";
  }

}
