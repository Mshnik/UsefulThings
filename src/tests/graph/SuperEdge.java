package graph;

import java.util.Objects;

/**
 * A default edge implementation for use in testing
 *
 * @author Mshnik
 */
class SuperEdge implements Weighted, Flowable, Copyable<SuperEdge> {
  public final String name;
  private int w;
  private int c;

  public SuperEdge(String n) {
    this.name = n;
  }

  public SuperEdge setWeight(int w) {
    this.w = w;
    return this;
  }

  public SuperEdge setCapacity(int c) {
    this.c = c;
    return this;
  }

  public int getWeight() {
    return w;
  }

  public int getCapacity() {
    return c;
  }

  public SuperEdge copy() {
    return new SuperEdge(name + "-copy").setWeight(w).setCapacity(c);
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
      return name.equals(s.name) && w == s.w && c == s.c;
    } catch (ClassCastException e) {
      return false;
    }
  }

  public int hashCode() {
    return Objects.hash(name, w,c);
  }
}
