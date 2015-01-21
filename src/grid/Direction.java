package grid;

import java.util.Arrays;

public abstract class Direction implements Cloneable{

	private final Integer[] delta;
	
	protected Direction(Integer... d){
		this.delta = Arrays.copyOf(d, d.length);
	}
	
	protected Direction(Direction... components){
		delta = new Integer[components[0].delta.length];
		Arrays.fill(delta, new Integer(0));
		
		for(Direction d : components){
			for(int i = 0; i < d.delta.length; i++){
				delta[i] += d.delta[i];
			}
		}
	}
	
	public abstract Direction clone();
	
	public Direction scale(int k){
		Direction d = clone();
		for(int i = 0; i < delta.length; i++){
			d.delta[i] *= k;
		}
		return d;
	}
	
	public Integer[] get(){
		return Arrays.copyOf(delta, delta.length);
	}
	
	public String toString(){
		return Arrays.deepToString(delta);
	}
	
}
