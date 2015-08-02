package common.math;

import java.util.Arrays;
import java.util.Iterator;

import common.Util;

import functional.FunctionalUtil;

public class Vector implements Iterable<Double> {

	private Double[] vec;
	private double magnitude;
	
	/** Returns an array filled with 0.0, with length equal to the length 
	 * of the longer input vector
	 */
	private Double[] longerVec(Vector v1, Vector v2){
		Double[] v = new Double[Math.max(v1.dimension(), v2.dimension())];
		Arrays.fill(v, 0);
		return v;
	}
	
	public Vector(){
		this.vec = new Double[]{};
		magnitude = -1;
	}
	
	
	public Vector(double[] components){
		this(Util.boxArr(components));
	}
	
	public Vector(Double... components){
		this.vec = Arrays.copyOf(components, components.length);
		magnitude = -1; //denotes uninitialized - lazily computed
	}
	
	public Vector(Vector... components){
		this(components[0].vec);
		for(int i = 1; i < components.length; i++){
			add(components[i]);
		}
	}
	
	public Vector add(Vector other){
		Vector v = new Vector(longerVec(this, other));
		for(int i = 0; i < dimension(); i++){
			v.vec[i] += vec[i];
		}
		for(int i = 0; i < other.dimension(); i++){
			v.vec[i] += other.vec[i];
		}
		return v;
	}
	
	public Vector add(Double... vec){
		return add(new Vector(vec));
	}
	
	public Vector invert(){
		Vector v = new Vector(vec);
		for(int i = 0; i < v.dimension(); i++){
			v.vec[i] = -1 * v.vec[i];
		}
		return v;
	}
	
	public Vector subtract(Vector other){
		return add(other.invert());
	}
	
	public Vector scale(double k){
		Vector v = new Vector(vec);
		for(int i = 0; i < v.dimension(); i++){
			v.vec[i] *= k;
		}
		return v;
	}
	
	public double dot(Vector other){
		return FunctionalUtil.foldLeft2(0.0, vec, other.vec, (a,b,c) -> a + b*c);
	}
	
	public final Double[] get(){
		return Arrays.copyOf(vec, vec.length);
	}
	
	public final Double get(int index){
		return vec[index];
	}
	
	public double magnitude(){
		if(magnitude >= 0) return magnitude;
		
		double d = 0;
		for(int i = 0; i < dimension(); i++){
			d += vec[i] * vec[i];
		}
		magnitude = Math.sqrt(d);
		return magnitude;
	}
	
	public final int dimension(){
		return vec.length;
	}
	
	@Override
	public String toString(){
		if(dimension() == 0)
			return "<>";
		String s = "<";
		for(int i = 0; i < dimension() - 1; i++){
			s += vec[i] + ", ";
		}
		return s + vec[dimension() - 1] + ">";
	}
	
	@Override
	public int hashCode(){
		return Arrays.hashCode(vec);
	}
	
	@Override
	public boolean equals(Object o){
		if(! (o instanceof Vector)) return false;
		return Arrays.equals(vec, ((Vector) o).vec);
	}

	@Override
	public Iterator<Double> iterator() {
		return new VectorIterator();
	}
	
	class VectorIterator implements Iterator<Double>{
		private int index = 0;

		@Override
		public boolean hasNext() {
			return index < dimension();
		}

		@Override
		public Double next() {
			return get(index++);
		}
	}
	
}
