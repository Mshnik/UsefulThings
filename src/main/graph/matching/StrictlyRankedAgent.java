package graph.matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO - SPEC
public interface StrictlyRankedAgent<X> extends RankedAgent<X> {

  /** Return a StrictlyRankedAgent with the given map of preferences */
  public static <X> StrictlyRankedAgent<X> create(List<X> items) {
    final List<X> itemsCpy = Collections.unmodifiableList(items);
    return () -> itemsCpy;
  }

  /**
   * Returns an unalterable List representing the preferences of this StrictlyRankedAgent.
   * items appear in the array in the order that this prefers them in.
   * Other items not in the array are unacceptable to this StrictlyRankedAgent.
   * <br>
   * {@code a(0) > a(1) > a(2) > ... > a(n-1) > everything else}
   */
  public List<X> getStrictPreferences();

  /**
   * Returns an unmodifiable Map relating each X this finds acceptable to an Integer describing
   * how well that item ranks.
   * Higher valued items are preferred to lower valued items.
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
}
