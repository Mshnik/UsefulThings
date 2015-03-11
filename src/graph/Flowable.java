package graph;

/** Designates an element that can have flow directed along it in a network
 * flow setting
 * @author MPatashnik
 *
 */
public interface Flowable {

	/** Returns the maximum capacity of this Flowable. 
	 * Must return a non-negative value */
	public int getCapacity();
	
}
