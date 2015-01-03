package grid;


public class Tile{

	/** The location of this tile in its grid */
	protected final Location loc;
	
	/** The Grid this Tile belongs to. Null until this is added to a grid */
	protected Grid grid;
	
	private static TileBuilder currentBuilder = new TileBuilder();
	
	/** Starts building a new tile. */
	public static TileBuilder startBuilding(Integer... location){
		return currentBuilder.reset().setLocation(location);
	}
	
	/** Returns the Builder object */
	public static TileBuilder getBuilder(){
		return currentBuilder;
	}
	
	/** Completely wipes Builder settings */
	public static TileBuilder wipeBuilder(){
		return currentBuilder.resetHard();
	}
	
	private Tile(Location l){
		loc = l;
	}
	
	/** Builder class for builder paradigm for construction */
	public static class TileBuilder{
		
		private String[] locationLabels;  //Hard
		private Integer[] locationVals;   //Soft
		
		/** Resets this TileBuilder to its default settings (for creating a new tile).
		 * Only clears soft fields */
		public TileBuilder reset(){
			locationVals = null;
			return this;
		}
		
		/** Resets this TileBuilder to its initial settings (for creating a new tile).
		 * Clears all fields */
		public TileBuilder resetHard(){
			locationVals = null;
			locationLabels = null;
			return this;
		}
		
		/** Sets the Location vals of the TileBuilder */
		public TileBuilder setLocation(Integer... vals){
			locationVals = vals;
			return this;
		}
		
		/** Sets the Location Labels of the TileBuilder */
		public TileBuilder setLocationLabels(String... labels){
			locationLabels = labels;
			return this;
		}
		
		/** Builds and returns a new tile from the compiled settings
		 * Sets the currentBuilder in class Tile to null
		 * @return - a new Tile
		 */
		public Tile build(){
			return new Tile(new Location(locationLabels, locationVals));
		}
		
	}
	
	
}
