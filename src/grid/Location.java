package grid;

import java.util.Arrays;
import java.util.HashMap;

public class Location implements Cloneable{

	private HashMap<String, Integer> valsMap;
	private String[] labels;
	private Integer[] vals;
	
	/** Constructs a Location Object
	 * @param labels - the labels for the ints of this Location.
	 * @param vals - the integers that represent this Location, in the correct order
	 * 				must be the same number of inputs as the length of this.getLabelArray(),
	 * 				the length of which may depend by implementation
	 */
	public Location(String[] labels, Integer... vals){
		if(vals.length != labels.length)
			throw new IllegalArgumentException("Vals array " + vals + 
					" and Labels array " + labels + " unequal length.");

		this.vals = Arrays.copyOf(vals, vals.length);
		this.labels = Arrays.copyOf(labels, labels.length);
		valsMap = new HashMap<String, Integer>();
		for(int i = 0; i < vals.length; i++){
			valsMap.put(labels[i], vals[i]);
		}
	}
	
	/** Constructs a Location Object that is a clone of this one */
	@Override
	public Location clone(){
		return new Location(labels, vals);
	}
	
	/** Returns the location this corresponds to */
	public final int[] getVals(){
		int[] i = new int[vals.length];
		for(int x = 0; x < i.length; x++){
			i[x] = vals[x];
		}
		return i;
	}
	
	/** Returns the length (number of vals) of this Location */
	public int getLength(){
		return vals.length;
	}
	
	/** Returns the manhattan distance to the given tile (difference in each coordinate) */
	public int getManhattanDistance(Location l) throws IllegalArgumentException{
		if(l.getLength() != getLength())
			throw new IllegalArgumentException("Can't compare " + l + " to " + this);
		int diff = 0;
		for(int i = 0; i < getLength(); i++){
			diff += Math.abs(vals[i] - l.vals[i]);
		}
		return diff;
	}
	
	/** Returns true iff there is a component of this that is less than the
	 * corresponding component in the minVals array
	 * @param minArry - an array of integers with the same length as this.getLength()
	 */
	protected boolean underMin(int[] minArr) throws IllegalArgumentException{
		if(minArr.length != getLength())
			throw new IllegalArgumentException("Bad minArr " + minArr + " for " 
					+ this + " wrong length");
		for(int i = 0; i < minArr.length; i++){
			if(vals[i] < minArr[i])
				return true;
		}
		return false;
	}
	
	/** Returns true iff there is a component of this that is greater than the
	 * corresponding component in the maxVals array
	 * @param maxArr - an array of integers with the same length as this.getLength()
	 */
	protected boolean overMax(int[] maxArr) throws IllegalArgumentException{
		if(maxArr.length != getLength())
			throw new IllegalArgumentException("Bad minArr " + maxArr + " for " 
					+ this + " wrong length");
		for(int i = 0; i < maxArr.length; i++){
			if(vals[i] > maxArr[i])
				return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return valsMap.toString();
	}
	
	@Override
	public final int hashCode(){
		return Arrays.hashCode(vals);
	}
	
	@Override
	public final boolean equals(Object o){
		if(! (o instanceof Location)) return false;
		return Arrays.deepEquals(vals, ((Location)o).vals);
	}
}
