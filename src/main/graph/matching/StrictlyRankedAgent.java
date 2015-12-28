package graph.matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface StrictlyRankedAgent<X> extends RankedAgent<X> {

  /**
   * Returns an array representing the preferences of this StrictlyRankedAgent.
   * items appear in the array in the order that this prefers them in.
   * Other items not in the array are unacceptable to this StrictlyRankedAgent.
   * <br>
   * {@code a[0] > a[1] > a[2] > ... > a[n] > everything else}
   */
  public List<X> getStrictPreferences();

  /**
   * A helper method to implement RankedAgent.getPreferences, given that
   * getPreferences is written. Allows classes implementing StrictlyRankedAgent
   * to simply return createPreferences(this) for the getPreferences method.
   * If preferences never change, this method can be called at construction
   * time and the same map returned each call. The returned map is unmodifiable
   */
  default Map<X, Integer> getPreferences() {
    HashMap<X, Integer> map = new HashMap<>();
    List<X> lst = getStrictPreferences();
    int i = lst.size();
    for (X x : lst) {
      map.put(x, i);
      i--;
    }
    return Collections.unmodifiableMap(map);
  }

  /**
   * Returns the list of agents that a strictly prefers to x, in order
   * of preference, with the highest ranked item coming first
   */
  default List<X> perfersList(X x) {
    LinkedList<X> h = new LinkedList<X>();
    for (X x2 : getStrictPreferences()) {
      if (x2.equals(x))
        break;
      h.add(x2);
    }
    return Collections.unmodifiableList(h);
  }

  /**
   * Returns the set of agents that a weakly prefers to x. Will contain x if
   * x is acceptable to a.
   */
  default List<X> perfersWeaklyList(X x) {
    LinkedList<X> h = new LinkedList<X>();
    for (X x2 : getStrictPreferences()) {
      if (x2.equals(x)) {
        h.add(x);
        break;
      }
      h.add(x2);
    }
    return Collections.unmodifiableList(h);
  }
}
