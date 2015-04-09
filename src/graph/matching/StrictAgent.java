package graph.matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface StrictAgent<X> extends Agent<X>{

	/** Returns an array representing the preferences of this StrictAgent.
	 * items appear in the array in the order that this prefers them in.
	 * Other items not in the array are unacceptable to this StrictAgent.
	 * <br>
	 * {@code a[0] > a[1] > a[2] > ... > a[n] > everything else}
	 */
	public X[] getRankPreferences();
	
	/** A helper method to implement Agent.getPreferences, given that 
	 * getPreferences is written. Allows classes implementing StrictAgent
	 * to simply return createPreferences(this) for the getPreferences method.
	 * If preferences never change, this method can be called at construction
	 * time and the same map returned each call. The returned map is unmodifiable
	 */
	public static <X> Map<X, Integer> createPreferences(StrictAgent<X> a){
		HashMap<X, Integer> map = new HashMap<>();
		X[] arr = a.getRankPreferences();
		for(int i = 0; i < arr.length; i++){
			map.put(arr[i], arr.length-i);
		}
		return Collections.unmodifiableMap(map);
	}
	
	/** Returns the list of agents that a strictly prefers to x, in order
	 * of preference, with the highest ranked item coming first */
	public static <X> List<X> perfersList(StrictAgent<X> a, X x){
		LinkedList<X> h = new LinkedList<X>();
		for(X x2 : a.getRankPreferences()){
			if(x2.equals(x))
				break;
			h.add(x2);
		}
		return Collections.unmodifiableList(h);
	}
	
	/** Returns the set of agents that a weakly prefers to x. Will contain x if
	 * x is acceptable to a. */
	public static <X> List<X> perfersWeaklyList(StrictAgent<X> a, X x){
		LinkedList<X> h = new LinkedList<X>();
		for(X x2 : a.getRankPreferences()){
			if(x2.equals(x)){
				h.add(x);
				break;
			}
			h.add(x2);
		}
		return Collections.unmodifiableList(h);
	}
	
}
