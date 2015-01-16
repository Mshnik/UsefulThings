package grid;

import grid.Location.LocationBuilder;
import grid.Location.LocationOutOfBoundsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Grid<T extends Tile> extends HashMap<Location, T>{

	/***/
	private static final long serialVersionUID = 1L;

	private final int[] minVals;
	private final int[] maxVals;
	
	/** Initializes an empty grid
	 * @param min - an array of min coordinate values
	 * @param max - an array of max coordinate values
	 */
	public Grid(int[] min, int[] max){
		if(min.length != max.length)
			throw new IllegalArgumentException("Incompatibile min " + min +", max " + max + " arrs");
		for(int i = 0; i < min.length; i++){
			if(min[i] > max[i])
				throw new IllegalArgumentException("Incompatibile min " + min +", max " + max + " arrs");
		}
		minVals = Arrays.copyOf(min, min.length);
		maxVals = Arrays.copyOf(max, max.length);
	}
	
	/** Returns the minimum values for coordinates in this grid */
	public int[] getMinVals(){
		return Arrays.copyOf(minVals, minVals.length);
	}
	
	/** Returns the maximum values for coordinates in this grid */
	public int[] getMaxVals(){
		return Arrays.copyOf(maxVals, maxVals.length);
	}
	
	/** Puts the given tile at the given location.
	 * @param loc - the key to put at
	 * @param t - the value to put. t.loc must equal loc.
	 * @throws IllegalArgumentException if: <br>
	 * 				loc isn't the right length for locations in this grid <br>
	 * 				{@code ! t.loc.equals(loc)} <br>
	 * @throws LocationOutOfBoundsException if loc is OOB for this grid
	 */
	@Override
	public T put(Location loc, T t) throws IllegalArgumentException, LocationOutOfBoundsException{
		if(loc.getLength() != minVals.length)
			throw new IllegalArgumentException("Location " + loc + " not compatibile with Grid " + this);
		if(! loc.equals(t.loc))
			throw new IllegalArgumentException("Can't put Tile " + t + " at wrong location " + loc);
		if(loc.underMin(minVals) || loc.overMax(maxVals))
			throw new LocationOutOfBoundsException("Can't put at Location " + loc + ": OOB");
		return super.put(loc, t);
	}
	
	/** Adds t to this grid. Calls {@code put(t.loc, t)}. Can't overwrite a tile though */
	public boolean addTile(T t) throws NotInGridException{
		if(containsKey(t.loc))
			throw new NotInGridException("Can't add " + t + " already have tile at "
					+ t.loc + "; remove tile " + get(t.loc) + " first.");
		put(t.loc, t);
		return true;
	}
	
	/** Removes {@code t} from this grid, throwing exception if t isn't in this grid */
	public boolean removeTile(T t) throws NotInGridException{
		if(! containsKey(t.loc))
			throw new NotInGridException("Can't Remove " + t + "; not in this grid");
		
		remove(t.loc);
		return true;
	}
	
	/** Returns the neighbors (adjacent tiles) of {@code t}.
	 * @param t - the tile to get neighbors of
	 * @param diagonals - true if tiles that are diagonal from t (more than one delta) should be included
	 * 					  false for just cardinal directions
	 * @return - the neighbors of t
	 * @throws NotInGridException - if t isn't contained in this grid
	 */
	public ArrayList<T> getNeighbors(T t, boolean diagonals) throws NotInGridException{
		if(! containsKey(t.loc))
			throw new NotInGridException("Can't get neighbors of " + t + "; not in this grid");
		ArrayList<T> neighbors = new ArrayList<>();
		if(diagonals){
			ArrayList<int[]> is = recBuild(new ArrayList<int[]>(), t.loc.getVals(), 
					new int[]{-1, 0, 1}, 0);
			LocationBuilder lb = Location.initBuilder(minVals, maxVals, t.loc.getLabels());
			for(int[] i : is){
				try{
					T t2 = get(lb.build(i));
					if(t2 != null && t2 != t) neighbors.add(t2);
				}catch(LocationOutOfBoundsException e){}
			}
		} else{
			for(int i = 0; i < minVals.length; i++){
				T t2 = get(t.loc.cloneWithChange(i, 1));
				if(t2 != null) neighbors.add(t2);
				t2 = get(t.loc.cloneWithChange(i, -1));
				if(t2 != null) neighbors.add(t2);
			}
		}
		return neighbors;
	}
	
	/** Returns all coordinate groups that are in bounds for this grid */
	public ArrayList<Integer[]> buildCoordinates(){
		return recBuild(new Integer[minVals.length], new ArrayList<Integer[]>(), 0, minVals, maxVals);
	}
	
	/** Recursively builds int arrays within the given ranges
	 * @param template
	 * @param built
	 * @param depth
	 * @param minVal
	 * @param maxVal
	 * @return
	 */
	private static ArrayList<Integer[]> recBuild(Integer[] template, 
			ArrayList<Integer[]> built, int depth, int[] minVal, int[] maxVal){
		if(depth == minVal.length){
			built.add(Arrays.copyOf(template, template.length));
			return built;
		}
		for(int i = minVal[depth]; i <= maxVal[depth]; i++){
			template[depth] = i;
			recBuild(template, built, depth+1, minVal, maxVal);
		}
		template[depth] = null;
		return built;
	}
	
	/** Recursively builds int arrays
	 * @param built - the arrays built thus far
	 * @param template - the array being buit
	 * @param deltas - the deltas to apply to each index of the template
	 * @param index - the current index. Also keeps track of recursion depth
	 * @return - many int arrays. Yeah.
	 */
	private static ArrayList<int[]> recBuild(ArrayList<int[]> built, 
			int[] template, int[] deltas, int index){
		if(index == template.length){
			built.add(template);
			return built;
		}
		for(int d : deltas){
			int[] templateNew = Arrays.copyOf(template, template.length);
			templateNew[index] += d;
			recBuild(built, templateNew, deltas, index+1);
		}
		return built;
	}
	
	static class NotInGridException extends RuntimeException{
		/***/
		private static final long serialVersionUID = 1L;

		public NotInGridException(String s){
			super(s);
		}
		
	}
	
}
