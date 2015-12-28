package graph.matching;

import java.util.*;
import java.util.Map.Entry;

//TODO - TEST
public interface RankedAgent<X> extends Agent<X> {

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

  /**
   * Returns the set of X that this RankedAgent strictly prefers to x.
   * May return an empty set if x is this RankedAgent's most preferred choice.
   * If x is not acceptable to this RankedAgent, returns the set of all
   * acceptable items
   */
  default Set<X> perfersSet(X x) {
    HashSet<X> h = new HashSet<X>();
    Map<X, Integer> pref = getPreferences();
    int val = pref.getOrDefault(x, Integer.MIN_VALUE);
    for (Entry<X, Integer> e : pref.entrySet()) {
      if (e.getValue() > val) h.add(e.getKey());
    }
    return Collections.unmodifiableSet(h);
  }

  /**
   * Returns the set of X that this RankedAgent weakly prefers to x.
   * Will always return a set of at least size 1.
   * If x is not acceptable to this RankedAgent, returns the set of all
   * acceptable items
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
