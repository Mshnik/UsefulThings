package graph;

/** A Copyable is able to create copies (not clones) of itself for graph
 * algorithm purposes. A copy is a new instance that is functionality identical
 * for graph algorithm purposes, but, specifically, a.equals(a.copy()) should 
 * return false. This allows for the creation of extra edges and vertices
 * that behave the same as the original, but are distinct objects
 * for hashsets and map storage.
 * <br><br>
 * If a Copyable overwrites the Object class' equals method or hashcode method,
 * it should in part rely on the return of getID() in order to guarantee the above
 * behavior.
 * @author Mshnik
 * @param <E> - The thing this is copyable to. 
 * Should be the class that implements this (like comparable).
 */
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
	
}
