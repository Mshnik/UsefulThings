package graph.matching;

import java.util.Map;

public interface Agent<X> {

	/** Returns an integer value for each X. All
	 * X not in the returned map are presumed to have a preference value of 
	 * Integer.MIN_VALUE when that value is needed.
	 * Higher valued items are preferred to lower valued agents, and
	 * Equally valued items are interchangeable.
	 * The returned map should be unmodifiable
	 */
	public Map<X, Integer> getPreferences();
	
	/** Returns true iff x is acceptable to a - if x appears anywhere 
	 * in a's preference list
	 */
	public static <X> boolean isAcceptable(Agent<X> a, X x){
		return a.getPreferences().containsKey(x);
	}
	
	/** Returns true iff a prefers x1 to x2, strictly.
	 * Returns false if x1.equals(x2). Also returns false
	 * if neither x1 nor x2 are acceptable to a.
	 */
	public static <X> boolean prefers(Agent<X> a, X x1, X x2){
		Map<X, Integer> pref = a.getPreferences();
		return pref.getOrDefault(x1, Integer.MIN_VALUE) > pref.getOrDefault(x2, Integer.MIN_VALUE);
	}
	
	/** Returns true iff a weakly prefers x1 to x2. */
	public static <X> boolean prefersWeakly(Agent<X> a, X x1, X x2){
		Map<X, Integer> pref = a.getPreferences();
		return pref.getOrDefault(x1, Integer.MIN_VALUE) >= pref.getOrDefault(x2, Integer.MIN_VALUE);
	}
	
}

