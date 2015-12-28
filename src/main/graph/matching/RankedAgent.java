package graph.matching;

import java.util.*;
import java.util.Map.Entry;

//TODO - SPEC
//TODO - TEST
public interface RankedAgent<X> extends Agent<X> {

  //TODO - SPEC
  /**
   * Returns an integer value for each X. All
   * X not in the returned map are presumed to have a preference value of
   * Integer.MIN_VALUE when that value is needed.
   * Higher valued items are preferred to lower valued agents, and
   * Equally valued items are interchangeable.
   * The returned map should be unmodifiable
   */
  public Map<X, Integer> getPreferences();

  //TODO - SPEC
  default Set<X> getAcceptableItems() {
    return getPreferences().keySet();
  }

  //TODO - SPEC
  /**
   * Returns true iff a prefers x1 to x2, strictly.
   * Returns false if x1.equals(x2). Also returns false
   * if neither x1 nor x2 are acceptable to a.
   */
  default boolean prefers(X x1, X x2) {
    Map<X, Integer> pref = getPreferences();
    return pref.getOrDefault(x1, Integer.MIN_VALUE) > pref.getOrDefault(x2, Integer.MIN_VALUE);
  }

  //TODO - SPEC
  /**
   * Returns true iff a weakly prefers x1 to x2.
   */
  default boolean prefersWeakly(X x1, X x2) {
    Map<X, Integer> pref = getPreferences();
    return pref.getOrDefault(x1, Integer.MIN_VALUE) >= pref.getOrDefault(x2, Integer.MIN_VALUE);
  }

  //TODO - SPEC
  /**
   * Returns the set of agents that a strictly prefers to x
   */
  default Collection<X> perfersSet(X x) {
    HashSet<X> h = new HashSet<X>();
    Map<X, Integer> pref = getPreferences();
    int val = pref.getOrDefault(x, Integer.MIN_VALUE);
    for (Entry<X, Integer> e : pref.entrySet()) {
      if (e.getValue() > val) h.add(e.getKey());
    }
    return Collections.unmodifiableSet(h);
  }

  //TODO - SPEC
  /**
   * Returns the set of agents that a weakly prefers to x. Will contain x if
   * x is acceptable to a.
   */
  default Collection<X> perfersWeaklySet(X x) {
    HashSet<X> h = new HashSet<X>();
    Map<X, Integer> pref = getPreferences();
    int val = pref.getOrDefault(x, Integer.MIN_VALUE);
    for (Entry<X, Integer> e : pref.entrySet()) {
      if (e.getValue() >= val) h.add(e.getKey());
    }
    return Collections.unmodifiableSet(h);
  }

}

