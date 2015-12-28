package graph.matching;

import java.util.Set;

//TODO - TEST
public interface Agent<X> {

  /** Return the set of items that this agent finds acceptable.
   * This Agent prefers all items in this set to
   * all items not within this set, and is indifferent among items not in this set.
   * This Agent may or may not have a preference order among the items in this Set
   * The returned set should be unalterable.
   */
  public Set<X> getAcceptableItems();

  /** Returns true iff x is acceptable - if x appears anywhere in this' preference list */
  default boolean isAcceptable(X x) {
    return getAcceptableItems().contains(x);
  }
}
