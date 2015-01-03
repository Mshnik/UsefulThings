package grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Grid extends HashMap<Location, Tile>{


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
	
	/** Initializes a full grid of default tiles
	 * @param min - an array of min coordinate values
	 * @param max - an array of max coordinate values
	 * @param labels - the labels on the corrdinates for all tiles in this Grid
	 */
	public Grid(int[] min, int[] max, String[] labels){
		this(min, max);
		if(labels.length != min.length)
			throw new IllegalArgumentException("Incompatibile labels " + labels + " with min " + min);
		
		//Set default properties
		Tile.wipeBuilder().setLocationLabels(labels);
		
		for(Integer[] v : buildCoordinates(min, max)){
			Tile t = Tile.getBuilder().setLocation(v).build();
			addTile(t);
		}	
	}
	
	private static ArrayList<Integer[]> buildCoordinates(int[] minVal, int[] maxVal){
		if(minVal.length != maxVal.length)
			throw new IllegalArgumentException("Incompatibile min " + minVal +", max " + maxVal + " arrs");
		return recBuild(new Integer[minVal.length], new ArrayList<Integer[]>(), 0, minVal, maxVal);
	}
	
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
	
	@Override
	public Tile put(Location loc, Tile t) throws IllegalArgumentException{
		if(loc.getLength() != minVals.length)
			throw new IllegalArgumentException("Location " + loc + " not compatibile with Grid " + this);
		if(! loc.equals(t.loc))
			throw new IllegalArgumentException("Can't put Tile " + t + " at wrong location " + loc);
		if(loc.underMin(minVals) || loc.overMax(maxVals))
			throw new IllegalArgumentException("Can't put at Location " + loc + ": OOB");
		return super.put(loc, t);
	}
	
	public boolean addTile(Tile t) throws RuntimeException{
		if(containsKey(t.loc))
			throw new RuntimeException("Can't add " + t + " already have tile at "
					+ t.loc + "; remove tile " + get(t.loc) + " first.");
		put(t.loc, t);
		return true;
	}
	
	public boolean removeTile(Tile t) throws RuntimeException{
		if(! containsKey(t.loc))
			throw new RuntimeException("Can't Remove " + t + "; not in this grid");
		
		remove(t.loc);
		return true;
	}
	
}
