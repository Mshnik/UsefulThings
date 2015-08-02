package grid;

public interface Tile{

	/** Returns the location of this Tile in the grid it is stored in.
	 * Shouldn't change once instantiated. Treat it like a hashcode
	 */
	public Integer[] getLocation();
}
