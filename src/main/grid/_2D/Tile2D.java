package grid._2D;

import grid.Tile;

public interface Tile2D extends Tile {

	/** Should return {row, col}. */
	@Override
	public Integer[] getLocation();
	
	/** Returns the row this is located at in its grid */
	public int getRow();
	
	/** Returns the col this is located at in its grid */
	public int getCol();
	
}
