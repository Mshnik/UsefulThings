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
	 */
	public Grid(Integer... bounds){
		dimension = bounds.length;
		this.bounds = new int[dimension];
		for(int i = 0; i < dimension; i++){
			this.bounds[i] = bounds[i];
		}
		vals = recCreateArrays(0);
	}
	
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
	
	public T get(Integer... loc){
		return recGetTile (vals, 0, loc);
	}
	
	public boolean containsAt(Integer... loc){
		return get(loc) != null;
	}
	
	public boolean contains(T t){
		return t == get(t.loc);
	}
	
	public void add(T t){
		recSetTile(vals, 0, t.loc, t);
	}
	
	public void remove(T t){
		recSetTile(vals, 0, t.loc, null);
	}
	
	@SuppressWarnings("unchecked")
	private T recGetTile(Object[] arr, int depth, Integer[] loc){
		int index = loc[depth];
		if(depth == dimension - 1) {
			return (T) arr[index];
		} else{
			return recGetTile((Object[])arr[index], depth + 1, loc);
		}
	}
	
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

	public String toString(){
		return Arrays.deepToString(vals);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new GridIterator();
	}
	
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
