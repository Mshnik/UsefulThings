package common;

import java.util.Collection;
import java.util.Iterator;

public class Util {

	/** Returns a random element from the given iterable.
	 * Useful for getting random element from non-indexed collections, like
	 * HashSets.
	 * <br><br>
	 * Synchronizes on the given col to prevent concurrent modification.
	 * Returns null if col is null or empty.
	 */
	public static <E> E randomElement(Collection<E> col){
		if(col == null)
			return null;
		
		Iterator<E> iterator;
		synchronized(col){
			
			if(col.isEmpty())
				return null;
			
			iterator = col.iterator();
			int r = (int)(Math.random() * col.size());
			for(int i = 0; i < r; i++){ iterator.next(); }
		}
		return iterator.next();
	}
	
}
