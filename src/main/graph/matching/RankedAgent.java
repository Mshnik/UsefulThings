package graph.matching;

import java.util.*;
import java.util.Map.Entry;

//TODO - SPEC
public interface RankedAgent<X> extends Agent<X> {

  /** Return a RankedAgent with the given map of preferences */
  public static <X> RankedAgent<X> create(Map<X, Integer> items) {
    final Map<X, Integer> itemsCpy = Collections.unmodifiableMap(items);
    return () -> itemsCpy;
  }

  /** Returns a Map relating each X this finds acceptable to an Integer describing
   * how well that item ranks.
   * Higher valued items are preferred to lower valued items, and
   * Equally valued items are interchangeable.
   * The returned map should be unmodifiable.
   */
  public Map<X, Integer> getPreferences();

  /**
   * Return the set of items that this agent finds acceptable.
   * The returned set is unalterable.
   */
  default Set<X> getAcceptableItems() {
    return Collections.unmodifiableSet(getPreferences().keySet());
  }

  /**
   * Returns true iff this RankedAgent prefers x1 to x2, strictly.
   * Returns false if x1.equals(x2). Also returns false
   * if neither x1 nor x2 are acceptable to this RankedAgent.
   */
  default boolean prefers(X x1, X x2) {
    Map<X, Integer> pref = getPreferences();
    return pref.getOrDefault(x1, Integer.MIN_VALUE) > pref.getOrDefault(x2, Integer.MIN_VALUE);
  }

  /**
   * Returns true iff this RankedAgent prefers x1 to x2, weakly.
   * Returns true if x1.equals(x2). Also returns true
   * if neither x1 nor x2 are acceptable to this RankedAgent.
   */
  default boolean prefersWeakly(X x1, X x2) {
    Map<X, Integer> pref = getPreferences();
    return pref.getOrDefault(x1, Integer.MIN_VALUE) >= pref.getOrDefault(x2, Integer.MIN_VALUE);
  }
}
