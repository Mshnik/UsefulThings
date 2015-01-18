package grid;

public interface Tile{

	/** Returns the location of this Tile in the grid it is stored in.
	 * Shouldn't change once instantiated
	 */
	public Integer[] getLocation();
}
