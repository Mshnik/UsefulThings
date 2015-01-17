package grid;

@SuppressWarnings("rawtypes")
public interface Tile{

	/** Returns the location of this Tile in the grid it is stored in.
	 * Shouldn't change once instantiated
	 */
	public Integer[] getLocation();
	
	/** Returns the Grid this Tile is stored in, if any */
	public Grid getGrid();
}
