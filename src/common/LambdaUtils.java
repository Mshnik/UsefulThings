package common;

import java.util.Arrays;
import java.util.function.*;

public class LambdaUtils {

  private LambdaUtils(){}

  /** Applies f to each element in arr
   * @param arr - an array of values.
   * @param f - a consumer function
   */
  public static void forEach(int[] arr, IntConsumer f){
  	for(int i = 0; i < arr.length; i++){
  		f.accept(arr[i]);
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
  
  /** Applies f to each element in arr
   * @param arr - an array of values.
   * @param f - a consumer function
   */
  public static void forEach(double[] arr, DoubleConsumer f){
  	for(int i = 0; i < arr.length; i++){
  		f.accept(arr[i]);
  	}
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
  
  /** Applies f to each element in arr
   * @param arr - an array of values.
   * @param f - a consumer function
   */
  public static void forEach(long[] arr, LongConsumer f){
  	for(int i = 0; i < arr.length; i++){
  		f.accept(arr[i]);
  	}
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
  
  /** Applies f to each element in arr
   * @param arr - an array of values.
   * @param f - a consumer function
   */
  public static <T> void forEach(T[] arr, Consumer<T> f){
  	for(int i = 0; i < arr.length; i++){
  		f.accept(arr[i]);
  	}
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

}
