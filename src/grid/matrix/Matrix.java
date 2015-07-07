package grid.matrix;

import grid.Grid;

import java.util.Arrays;

public class Matrix extends Grid<NumberTile<Double>> {

	public Matrix(Integer... bounds){
		super(bounds);
	}
	
	public Matrix(int[] bounds){
		super(bounds);
	}
	
	public boolean hasBounds(int[] bounds){
		return Arrays.equals(this.bounds, bounds);
	}
	
	public double getVal(Integer... loc){
		NumberTile<Double> tile = get(loc);
		if(tile != null) return tile.getVal();
		else return 0;
	}
	
	public Matrix add(Matrix m) {
		if(! hasBounds(m.bounds))
			throw new RuntimeException();
		
		Matrix mx = new Matrix(bounds);
		for(Integer[] loc : buildCoordinates()){
			mx.add(new NumberTile<Double>(getVal(loc) + m.getVal(loc), loc));
		}
		
		return mx;
	}
}
