package grid._2D;

import grid.Direction;

public class Direction2D extends Direction {

	public static final Direction2D LEFT = new Direction2D(-1, 0);
	public static final Direction2D RIGHT = new Direction2D(1, 0);
	public static final Direction2D UP = new Direction2D(0, -1);
	public static final Direction2D DOWN = new Direction2D(0, 1);
	
	public static final Direction2D UP_LEFT = new Direction2D(UP, LEFT);
	public static final Direction2D UP_RIGHT = new Direction2D(UP, RIGHT);
	public static final Direction2D DOWN_LEFT = new Direction2D(DOWN, RIGHT);
	public static final Direction2D DOWN_RIGHT = new Direction2D(DOWN, RIGHT);
	
	protected Direction2D(Integer... d){
		super(d);
	}
	
	protected Direction2D(Direction2D... d){
		super(d);
	}
	
	public Direction2D clone(){
		return new Direction2D(this);
	}
	
	public boolean isCardinal(){
		return this.equals(LEFT) || this.equals(RIGHT) || 
				this.equals(UP) || this.equals(DOWN);
	}
	
	public boolean isCardinalOrCorner(){
		return isCardinal() || this.equals(UP_LEFT) || this.equals(UP_RIGHT)
				|| this.equals(DOWN_LEFT) || this.equals(DOWN_RIGHT);
	}
	
	
}
