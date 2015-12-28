package graph.matching;

import java.util.Set;

public interface Agent<X> {

  public Set<X> getAcceptableItems();

  /**
   * Returns true iff x is acceptable to a - if x appears anywhere
   * in a's preference list
   */
  default boolean isAcceptable(X x) {
    return getAcceptableItems().contains(x);
  }
}
