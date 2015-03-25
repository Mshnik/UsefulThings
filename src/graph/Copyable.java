package graph;

public interface Copyable<E> {

	/** Makes a Copy of this Copyable. The returned copyable
	 * should behave identically to this for the purposes of graph algorithms
	 * but should be distinct with respect to equality.
	 * Towards these ends, this.getID() should not equal copy().getID().
	 */
	public E copy();
	
	/** Returns some kind of identifier of this cloneable. Can be
	 * a string or Integer or whatever. */
	public Object getID();
	
	/** Copyables must overwrite the equals method to make it depend on the getID()
	 * return (in addition to whatever else */
	public boolean equals(Object o);
	
	/** Copyables should overwrite the hashCode method to make it consistent
	 * with the equals method 
	 */
	public int hashCode();
	
}
