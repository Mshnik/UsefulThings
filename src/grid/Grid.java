package grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Grid<T extends Tile> implements Iterable<T>{
	
	private Object[] vals;
	private final int[] bounds;
	public final int dimension;
	
	/** Initializes an empty grid
	 * @param bounds - the bounds of this grid. This also determines the dimensionaity of the grid
	 */
	public Grid(Integer... bounds){
		dimension = bounds.length;
		this.bounds = new int[dimension];
		for(int i = 0; i < dimension; i++){
			this.bounds[i] = bounds[i];
		}
		vals = recCreateArrays(0);
	}
	
	/** Creates arrays of the given depth, up to the length of bounds
	 * Used as a helper during construction, shouldn't be used otherwise.
	 */
	private Object[] recCreateArrays(int depth){
		if(depth == bounds.length) return null;
		
		Object[] vals = new Object[bounds[depth]];
		for(int i = 0; i < bounds[depth]; i++){
			vals[i] = recCreateArrays(depth + 1);
		}
		return vals;
	}
	
	/** Returns the bounds of this grid */
	public int[] getBounds(){
		return Arrays.copyOf(bounds, bounds.length);
	}
	
	/** Returns the tile at the given location
	 * @param loc - the location as a set of coordinates to find a tile
	 * @throws ArrayIndexOutOfBoundsException if any of the coordinates are OOB, or too many coordinates
	 * @throws ClassCastException if too few coordinates
	 * @return the tile at the given location, or null if none.
	 */
	public T get(Integer... loc){
		return recGetTile (vals, 0, loc);
	}
	
	/** Returns true iff there is a tile at the given location
	 * @param loc - the location as a set of coordinates to find a tile
	 * @throws ArrayIndexOutOfBoundsException if any of the coordinates are OOB, or too many coordinates
	 * @throws ClassCastException if too few coordinates
	 * @return the tile at the given location, or null if none.
	 */
	public boolean containsAt(Integer... loc){
		return get(loc) != null;
	}
	
	/** Returns true iff this grid contains t at its location.
	 * Note that if t was added to this grid, but then had its location changed, this will return false.
	 * @param t - the tile to look for
	 * @return true iff t is in this grid.
	 */
	public boolean contains(T t){
		return t == get(t.getLocation());
	}
	
	/** Adds the given tile to this grid, at its location. This will overwrite any previous tile at 
	 * that location
	 * @param t - the tile to add
	 */
	public void add(T t){
		recSetTile(vals, 0, t.getLocation(), t);
	}
	
	/** Removes the given tile from this grid, if present
	 * @param t - the tile to attempt to remove
	 * @return true iff t was removed
	 */
	public boolean remove(T t){
		T t2 = get(t.getLocation());
		if(t == t2){
			recSetTile(vals, 0, t.getLocation(), null);
			return true;
		}
		return false;
	}
	
	/** Recursive helper for getting a tile from the grid
	 * @param arr - the current array to search
	 * @param depth - the current depth in the search
	 * @param loc - the full set of coordinates to search on
	 * @return - the tile at the given location, or null iff none
	 */
	@SuppressWarnings("unchecked")
	private T recGetTile(Object[] arr, int depth, Integer[] loc){
		int index = loc[depth];
		if(depth == dimension - 1) {
			return (T) arr[index];
		} else{
			return recGetTile((Object[])arr[index], depth + 1, loc);
		}
	}
	
	/** Recursive helper for setting a tile on the grid
	 * @param arr - the current array to search
	 * @param depth - the current depth in the search
	 * @param loc - the full set of coordinates to search on
	 * @param t - the tile to set. Can be null to clear the given position
	 */
	private void recSetTile(Object[] arr, int depth, Integer[] loc, T t){
		int index = loc[depth];
		if(depth == dimension - 1) {
			arr[index] = t;
		} else{
			recSetTile((Object[])arr[index], depth + 1, loc, t);
		}
	}

	
//	/** Returns the neighbors (adjacent tiles) of {@code t}.
//	 * @param t - the tile to get neighbors of
//	 * @param diagonals - true if tiles that are diagonal from t (more than one delta) should be included
//	 * 					  false for just cardinal directions
//	 * @return - the neighbors of t
//	 * @throws NotInGridException - if t isn't contained in this grid
//	 */
//	public ArrayList<T> getNeighbors(T t, boolean diagonals) throws NotInGridException{
//		if(! containsKey(t.loc))
//			throw new NotInGridException("Can't get neighbors of " + t + "; not in this grid");
//		ArrayList<T> neighbors = new ArrayList<>();
//		if(diagonals){
//			ArrayList<int[]> is = recBuild(new ArrayList<int[]>(), t.loc.getVals(), 
//					new int[]{-1, 0, 1}, 0);
//			LocationBuilder lb = Location.initBuilder(minVals, maxVals, t.loc.getLabels());
//			for(int[] i : is){
//				try{
//					T t2 = get(lb.build(i));
//					if(t2 != null && t2 != t) neighbors.add(t2);
//				}catch(LocationOutOfBoundsException e){}
//			}
//		} else{
//			for(int i = 0; i < minVals.length; i++){
//				T t2 = get(t.loc.cloneWithChange(i, 1));
//				if(t2 != null) neighbors.add(t2);
//				t2 = get(t.loc.cloneWithChange(i, -1));
//				if(t2 != null) neighbors.add(t2);
//			}
//		}
//		return neighbors;
//	}
	
	/** Returns all coordinate groups that are in bounds for this grid */
	public ArrayList<Integer[]> buildCoordinates(){
		return recBuild(new Integer[dimension], new ArrayList<Integer[]>(), 0, bounds);
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
			ArrayList<Integer[]> built, int depth, int[] bounds){
		if(depth == bounds.length){
			built.add(Arrays.copyOf(template, template.length));
			return built;
		}
		for(int i = 0; i < bounds[depth]; i++){
			template[depth] = i;
			recBuild(template, built, depth+1, bounds);
		}
		template[depth] = null;
		return built;
	}
	
//	/** Recursively builds int arrays
//	 * @param built - the arrays built thus far
//	 * @param template - the array being buit
//	 * @param deltas - the deltas to apply to each index of the template
//	 * @param index - the current index. Also keeps track of recursion depth
//	 * @return - many int arrays. Yeah.
//	 */
//	private static ArrayList<int[]> recBuild(ArrayList<int[]> built, 
//			int[] template, int[] deltas, int index){
//		if(index == template.length){
//			built.add(template);
//			return built;
//		}
//		for(int d : deltas){
//			int[] templateNew = Arrays.copyOf(template, template.length);
//			templateNew[index] += d;
//			recBuild(built, templateNew, deltas, index+1);
//		}
//		return built;
//	}

	/** Returns a deep string of the array represented by this grid as
	 * its toString
	 */
	public String toString(){
		return Arrays.deepToString(vals);
	}
	
	/** Returns an Iterator over the tiles in this grid
	 * Iterates in order of most inner dimensions first
	 */
	@Override
	public Iterator<T> iterator() {
		return new GridIterator();
	}
	
	/** A class for iterating over a grid
	 * @author MPatashnik
	 *
	 */
	class GridIterator implements Iterator<T>{
		private Integer[] pos;
		private boolean next;
		
		public GridIterator(){
			pos = new Integer[dimension];
			for(int i = 0; i < dimension; i++){
				pos[i] = 0;
			}
			next = true;
		}

		@Override
		public boolean hasNext() {
			return next;
		}

		@Override
		public T next() {
			while(! containsAt(pos)){
				if(! recInc(dimension - 1))
					throw new NoSuchElementException();
			}
			T t = get(pos);
			next = recInc(dimension - 1);
			return t;
		}
		
		private boolean recInc(int depth){
			if(depth == -1)
				return false;
			pos[depth]++;
			if(pos[depth] == bounds[depth]){
				pos[depth] = 0;
				return recInc(depth - 1);
			}
			return true;
		}

		@Override
		public void remove() {
			T t = next();
			Grid.this.remove(t);
		}
		
		
	}
	
}
