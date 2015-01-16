package grid;

@SuppressWarnings("rawtypes")
public class Tile{

	/** The location of this tile in its grid */
	protected final Location loc;
	
	/** The Grid this Tile belongs to */
	public final Grid grid;
	
	/** True if this tile is currently "selected" */
	protected boolean selected;
	
	public Tile(Grid g, Location l){
		loc = l;
		grid = g;
	}
	
	/** Returns iff this tile is selected */
	public boolean isSelected(){
		return selected;
	}
	
	/** Toggles the selected status of this tile */
	public void toggleSelected(){
		selected = ! selected;
	}
}
