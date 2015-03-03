package common.tuple;

import java.util.Arrays;

/** An abstract parent of all tuples. Tuples as implemented here are
 * are immutable.
 * Stores all tupled values in an Object array for basic method implementations
 * Contains default implementations of toString(), equals(..), and hashCode().
 * @author Mshnik
 *
 */
public abstract class AbsTuple {

	/** The objects stored in the tuple. Shouldn't be altered
	 * after initialization. If it must be, subclass should make
	 * sure to update its fields to reflect this */
	protected final Object[] vals;
	
	/** Constructor for the AbsTuple class. Takes the values stored in this tuple
	 * @param v - the values stored in this tuple
	 */
	public AbsTuple(Object... v){
		vals = v;
	}
	
	/** A basic toString for all tuples. Returns a comma separated list
	 * of the values stored in this tuple, with parenthesis around it
	 */
	public String toString(){
		String s = "(";
		for(Object o : vals){
			s += o +",";
		}
		return s.substring(0, s.length() - 1) + ")";
	}
	
	/** A basic equals for all tuples. Returns true iff this and o
	 * are both tuples of the same length (thus same tuple implementation)
	 * and store the same values
	 */
	public boolean equals(Object o){
		if(! (o instanceof AbsTuple)) return false;
		AbsTuple t = (AbsTuple)o;
		return vals.length == t.vals.length && Arrays.deepEquals(vals, t.vals);
	}
	
	/** A basic hashCode implementation for all tuples. Returns
	 * a hash based on the values stored within this tuple, including the
	 * order in which they are stored
	 */
	public int hashCode(){
		return Arrays.deepHashCode(vals);
	}
	
}
