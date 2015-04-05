package common;

import java.util.Collection;
import java.util.Iterator;

public class Util {

	/** Returns a random element from the given iterable.
	 * Useful for getting random element from non-indexed collections, like
	 * HashSets.
	 * <br><br>
	 * Synchronizes on the given col to prevent concurrent modification.
	 * Returns null if col is null or empty.
	 */
	public static <E> E randomElement(Collection<E> col){
		if(col == null)
			return null;
		
		Iterator<E> iterator;
		synchronized(col){
			
			if(col.isEmpty())
				return null;
			
			iterator = col.iterator();
			int r = (int)(Math.random() * col.size());
			for(int i = 0; i < r; i++){ iterator.next(); }
		}
		return iterator.next();
	}
	
	/** Returns the MATHEMATICAL Mod of the two arguments, A % B.
	 * FK java for having a nonsensical interpretation of the mod operator. */
	public static int mod(int a, int b){
		return ((a % b) + b) % b;
	}
	
	/** Returns an Integer array that is equivalent to the given int array */
	public static Integer[] boxArr(int[] arr){
		Integer[] arr2 = new Integer[arr.length];
		for(int i = 0; i < arr.length; i++){
			arr2[i] = arr[i];
		}
		return arr2;
	}
	
	/** Returns an Float array that is equivalent to the given float array */
	public static Float[] boxArr(float[] arr){
		Float[] arr2 = new Float[arr.length];
		for(int i = 0; i < arr.length; i++){
			arr2[i] = arr[i];
		}
		return arr2;
	}
	
	/** Returns an Double array that is equivalent to the given double array */
	public static Double[] boxArr(double[] arr){
		Double[] arr2 = new Double[arr.length];
		for(int i = 0; i < arr.length; i++){
			arr2[i] = arr[i];
		}
		return arr2;
	}
	
	/** Returns an Character array that is equivalent to the given char array */
	public static Character[] boxArr(char[] arr){
		Character[] arr2 = new Character[arr.length];
		for(int i = 0; i < arr.length; i++){
			arr2[i] = arr[i];
		}
		return arr2;
	}
	
	/** Returns an int array that is equivalent to the given Integer array */
	public static int[] unboxArr(Integer[] arr){
		int[] arr2 = new int[arr.length];
		for(int i = 0; i < arr.length; i++){
			arr2[i] = arr[i];
		}
		return arr2;
	}
	
	/** Returns an float array that is equivalent to the given Float array */
	public static float[] unboxArr(Float[] arr){
		float[] arr2 = new float[arr.length];
		for(int i = 0; i < arr.length; i++){
			arr2[i] = arr[i];
		}
		return arr2;
	}
	
	/** Returns an double array that is equivalent to the given Double array */
	public static double[] unboxArr(Double[] arr){
		double[] arr2 = new double[arr.length];
		for(int i = 0; i < arr.length; i++){
			arr2[i] = arr[i];
		}
		return arr2;
	}
	
	/** Returns an char array that is equivalent to the given Character array */
	public static char[] unboxArr(Character[] arr){
		char[] arr2 = new char[arr.length];
		for(int i = 0; i < arr.length; i++){
			arr2[i] = arr[i];
		}
		return arr2;
	}
	
}
