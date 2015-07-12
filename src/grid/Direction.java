package grid;

import java.util.Arrays;

/** A direction represents a delta vector in some dimensionality of space
 * Directions are immutable - any alteration to a direction returns 
 * a new Direction object with the alterations.
 * @author Mshnik
 */
public abstract class Direction implements Cloneable{

	private final Integer[] delta;
	
	/** Constructs a Direction with the given delta vector.
	 * The length of the vector determines the dimensionality of the Direction.
	 * @param d - the delta vector, in the form of an array of Ints.
	 */
	protected Direction(Integer... d){
		this.delta = Arrays.copyOf(d, d.length);
	}
	
	/** Constructs a Direction as the summation of the given Direction components.
	 * The dimensionality of this Direction is the maximum dimension of 
	 * all the Direction components.
	 * @param components - an array of sub Directions to sum together.
	 */
	protected Direction(Direction... components){
		int length = 0;
		for(Direction d : components){
			length = Math.max(d.delta.length, length);
		}
		
		delta = new Integer[length];
		Arrays.fill(delta, new Integer(0));
		
		for(Direction d : components){
			for(int i = 0; i < d.delta.length; i++){
				delta[i] += d.delta[i];
			}
		}
	}
	
	/** Returns a copy of this Direction. Should be overwritten
	 * in every non-abstract subclass to return a new object of that type
	 */
	public abstract Direction clone();
	
	/** Returns a modified version of this Direction in which each
	 * component has been multiplied by the given coefficient.
	 * @param k - the scaling coefficient
	 * @return - a new Direction, scaled by k.
	 */
	public Direction scale(double k){
		Direction d = clone();
		for(int i = 0; i < delta.length; i++){
			d.delta[i] = (int)((double)d.delta[i] * k);
		}
		return d;
	}
	
	/** Returns the Integer delta vector that makes up this Direction.
	 * The returned object is a copy, thus edits will not affect this Direction.
	 */
	public final Integer[] get(){
		return Arrays.copyOf(delta, delta.length);
	}
	
	/** Returns a string representation of this Direction, of its Delta Vector */
	@Override
	public String toString(){
		return Arrays.deepToString(delta);
	}
	
	/** Returns a hashCode for this Direction, based on its delta vector. */
	@Override
	public int hashCode(){
		return Arrays.deepHashCode(delta);
	}
	
	/** Returns true iff o instanceof Direction and this' delta vector and 
	 * o's delta vector are deeply equivalent. */
	@Override
	public boolean equals(Object o){
		if(! (o instanceof Direction)) return false;
		return Arrays.deepEquals(delta, ((Direction) o).delta);
	}
	
}
