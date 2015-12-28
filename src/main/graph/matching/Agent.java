package graph.matching;

import java.util.Set;

//TODO - SPEC
//TODO - TEST
public interface Agent<X> {

  //TODO - SPEC
  public Set<X> getAcceptableItems();

  /**
   * Returns true iff x is acceptable to a - if x appears anywhere
   * in a's preference list
   */
  default boolean isAcceptable(X x) {
    return getAcceptableItems().contains(x);
  }
}
