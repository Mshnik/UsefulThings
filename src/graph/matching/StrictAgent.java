package graph.matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface StrictAgent<X> extends Agent<X>{

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
	
}
