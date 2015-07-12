package grid;

/** An abstract cursor implementation that is able to select things
 * in any grid
 * @param <T> - the type of the elements in the matrix this is choosing from. */
public class Cursor<T extends Tile>{

	/** The elm the cursor is currently on */
	private T elm;

	/** The grid this cursor is selecting over */
	public final Grid<? extends T> grid;

	public Cursor(Grid<? extends T> grid, T startingElm){
		this.grid = grid;
		setElm(startingElm);
	}

	/** Returns the element this cursor is currently on */
	public T getElm(){
		return elm;
	}

	/** Sets the tile this cursor is on */
	public void setElm(T t){
		elm = t;
	}

	/** Returns the location in the grid this Cursor is currently on */
	public Integer[] getLocation(){
		return elm.getLocation();
	}

	/** Called internally whenever a move would occur.
	 * Do validation, return true if move is ok, false otherwise
	 * Basic implementation - move is ok so long as the destination is non-null.
	 * Can be completely overridden in subclasses.
	 */
	protected boolean willMoveTo(Direction d, T destination){
		return destination != null;
	};

	/** Call to move in the given direction, if possible */
	public void move(Direction d){
		T dest = null;
		try{
			dest = grid.getFrom(getElm(), d.get());
		}catch(ArrayIndexOutOfBoundsException e){}

		if(willMoveTo(d, dest)){
			setElm(dest);
			moved();
		}
	}

	/** Called after a move occurs to do painting and the like.
	 * Can be overridden, but this method should be called first
	 * before adding new behavior */
	protected void moved(){}

	/** Simple toString that works off of the toString of the selected element */
	@Override
	public String toString(){
		return "Cursor on " + elm.toString();
	}
}
