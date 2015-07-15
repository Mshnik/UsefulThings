package common;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongUnaryOperator;
import java.util.function.UnaryOperator;

import common.dataStructures.DeArrList;
import common.types.Tuple2;

public class Functional {

	private Functional(){}

	/** Applies f to each element in arr
	 * @param arr - an array of values.
	 * @param f - a consumer function
	 */
	public static void forEach(int[] arr, IntConsumer f){
		for(int i = 0; i < arr.length; i++){
			f.accept(arr[i]);
		}
	}
	
	/** Applies f to each element in arr
	 * @param arr - an array of values.
	 * @param f - a consumer function
	 */
	public static void forEach(double[] arr, DoubleConsumer f){
		for(int i = 0; i < arr.length; i++){
			f.accept(arr[i]);
		}
	}
	
	/** Applies f to each element in arr
	 * @param arr - an array of values.
	 * @param f - a consumer function
	 */
	public static void forEach(long[] arr, LongConsumer f){
		for(int i = 0; i < arr.length; i++){
			f.accept(arr[i]);
		}
	}
	
	/** Applies f to each element in arr
	 * @param arr - an array of values.
	 * @param f - a consumer function
	 */
	public static <T> void forEach(T[] arr, Consumer<T> f){
		for(int i = 0; i < arr.length; i++){
			f.accept(arr[i]);
		}
	}
	
	/** Applies f to each element in col
	 * @param arr - an array of values.
	 * @param f - a consumer function
	 */
	public static <T> void forEach(Iterable<T> col, Consumer<T> f){
		for(T t : col){
			f.accept(t);
		}
	}

	/** Applies f to each element in arr, putting the results into a new array
	 * @param arr - an array of values
	 * @param f - a consumer function
	 * @return - an array of the mapped values: [f(arr[0]), f(arr[1]), ....]
	 */
	public static int[] map(int[] arr, IntUnaryOperator f){
		int[] dest = Arrays.copyOf(arr, arr.length);
		for(int i = 0; i < arr.length; i++){
			dest[i] = f.applyAsInt(arr[i]);
		}
		return dest;
	}

	/** Applies f to each element in arr, putting the results into a new array
	 * @param arr - an array of values
	 * @param f - a consumer function
	 * @return - an array of the mapped values: [f(arr[0]), f(arr[1]), ....]
	 */
	public static double[] map(double[] arr, DoubleUnaryOperator f){
		double[] dest = Arrays.copyOf(arr, arr.length);
		for(int i = 0; i < arr.length; i++){
			dest[i] = f.applyAsDouble(arr[i]);
		}
		return dest;
	}

	/** Applies f to each element in arr, putting the results into a new array
	 * @param arr - an array of values
	 * @param f - a consumer function
	 * @return - an array of the mapped values: [f(arr[0]), f(arr[1]), ....]
	 */
	public static long[] map(long[] arr, LongUnaryOperator f){
		long[] dest = Arrays.copyOf(arr, arr.length);
		for(int i = 0; i < arr.length; i++){
			dest[i] = f.applyAsLong(arr[i]);
		}
		return dest;
	}

	/** Applies f to each element in arr, putting the results into a new array
	 * @param arr - an array of values
	 * @param f - a consumer function
	 * @return - an array of the mapped values: [f(arr[0]), f(arr[1]), ....]
	 */
	public static <T> T[] map(T[] arr, UnaryOperator<T> f){
		T[] dest = Arrays.copyOf(arr, arr.length);
		for(int i = 0; i < arr.length; i++){
			dest[i] = f.apply(arr[i]);
		}
		return dest;
	}
	
	/** Zips lst and lst2 together. If either list is longer, the extra 
	 * elements are in tuples with null values.
	 * @param lst - the first list to zip
	 * @param lst2 - the second list to zip
	 * @return - a zipped list of tuples. Some tuples may have a null value
	 * 					but no tuple will have both null values.
	 */
	public static <T, U> List<Tuple2<T,U>> zip(List<T> lst, List<U> lst2){
		DeArrList<Tuple2<T,U>> zLst = new DeArrList<>();
		Iterator<T> i = lst.iterator();
		Iterator<U> i2 = lst2.iterator();
		while(i.hasNext() && i2.hasNext()){
			zLst.add(new Tuple2<T,U>(i.next(), i2.next()));
		}
		while(i.hasNext()){
			zLst.add(new Tuple2<T,U>(i.next(), null));
		}
		while(i2.hasNext()){
			zLst.add(new Tuple2<T,U>(null, i2.next()));
		}
		return zLst;
	}
	
	
}
