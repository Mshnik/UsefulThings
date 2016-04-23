package graph;

import java.util.Objects;

/**
 * A default edge implementation for use in testing
 *
 * @author Mshnik
 */
class SuperEdge implements Weighted, Flowable, Copyable<SuperEdge> {
  public final String name;
  private final int w;

  public SuperEdge(String n, int w) {
    this.name = n;
    this.w = w;
  }

  public int getWeight() {
    return w;
  }

  public int getCapacity() {
    return w;
  }

  public SuperEdge copy() {
    return new SuperEdge(name + "-copy", w);
  }

  public String getID() {
    return name;
  }

  public String toString() {
    return name;
  }

  public boolean equals(Object o) {
    try {
      SuperEdge s = (SuperEdge) o;
      return name.equals(s.name) && w == s.w;
    } catch (ClassCastException e) {
      return false;
    }
  }

  public int hashCode() {
    return Objects.hash(name, w);
  }
}
