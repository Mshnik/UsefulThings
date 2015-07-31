package functional;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.*;

import common.dataStructures.DeArrList;
import common.types.Tuple2;

public class FunctionalUtil {
	
	/** Curries one argument into f.
	 * @param f - the trifunction to curry
	 * @param t - the argument to include into the curried function
	 * @return - the curried BiFunction
	 */
	public static <T,U,V,W> BiFunction<U,V,W> curry(TriFunction<T,U,V,W> f, T t) {
		return (u, v) -> f.apply(t, u, v);
	}
	
	/** Curries one argument into f.
	 * @param f - the bifunction to curry
	 * @param u - the argument to include into the curried function
	 * @return - the curried Function
	 */
	public static <U,V,W> Function<V,W> curry(BiFunction<U,V,W> f, U u) {
		return (v) -> f.apply(u, v);
	}
	
	/** Curries one argument into f.
	 * @param f - the function to curry
	 * @param u - the argument to include into the curried function
	 * @return - the curried Supplier
	 */
	public static <U,V,W> Supplier<W> curry(Function<V,W> f, V v) {
		return () -> f.apply(v);
	}
	
	/** Curries one argument into f.
	 * @param f - the triConsumer to curry
	 * @param t - the argument to include into the curried function
	 * @return - the curried biconsumer
	 */
	public static <T,U,V> BiConsumer<U,V> curry(TriConsumer<T,U,V> f, T t) {
		return (u, v) -> f.apply(t, u, v);
	}
	
	/** Curries one argument into f.
	 * @param f - the biconsumer to curry
	 * @param u - the argument to include into the curried function
	 * @return - the curried Consumer
	 */
	public static <U,V> Consumer<V> curry(BiConsumer<U,V> f, U u) {
		return (v) -> f.apply(u, v);
	}
	
	/** Curries one argument into f.
	 * @param f - the consumer to curry
	 * @param u - the argument to include into the curried function
	 * @return - the curried unit
	 */
	public static <V> Unit curry(Consumer<V> f, V v) {
		return () -> f.apply(v);
	}
	
	/** Returns a generic version of the given intConsumer */
	public static Consumer<Integer> asGeneric(IntConsumer intConsumer) {
		return (i) -> intConsumer.accept(i);
	}
	
	/** Returns a generic version of the given intFunction */
	public static UnaryOperator<Integer> asGeneric(IntUnaryOperator intFunction) {
		return (i) -> intFunction.applyAsInt(i);
	}
	
	/** Returns a generic version of the given intFunction */
	public static <R> Function<Integer, R> asGeneric(IntFunction<R> intFunction) {
		return (i) -> intFunction.apply(i);
	}
	
	/** Returns a generic version of the given intPredicate */
	public static Predicate<Integer> asGeneric(IntPredicate intPredicate) {
		return (i) -> intPredicate.test(i);
	}
	
	/** Returns a generic version of the given intSupplier */
	public static Supplier<Integer> asGeneric(IntSupplier intSupplier) {
		return () -> intSupplier.getAsInt();
	}
	
	/** Returns a generic version of the given longConsumer */
	public static Consumer<Long> asGeneric(LongConsumer longConsumer) {
		return (l) -> longConsumer.accept(l);
	}
	
	/** Returns a generic version of the given longFunction */
	public static UnaryOperator<Long> asGeneric(LongUnaryOperator longFunction) {
		return (l) -> longFunction.applyAsLong(l);
	}
	
	/** Returns a generic version of the given longFunction */
	public static <R> Function<Long, R> asGeneric(LongFunction<R> longFunction) {
		return (l) -> longFunction.apply(l);
	}
	
	/** Returns a generic version of the given longPredicate */
	public static Predicate<Long> asGeneric(LongPredicate longPredicate) {
		return (l) -> longPredicate.test(l);
	}
	
	/** Returns a generic version of the given doubleSupplier */
	public static Supplier<Long> asGeneric(LongSupplier longSupplier) {
		return () -> longSupplier.getAsLong();
	}
	
	/** Returns a generic version of the given doubleConsumer */
	public static Consumer<Double> asGeneric(DoubleConsumer doubleConsumer) {
		return (d) -> doubleConsumer.accept(d);
	}
	
	/** Returns a generic version of the given doubleFunction */
	public static UnaryOperator<Double> asGeneric(DoubleUnaryOperator doubleFunction) {
		return (d) -> doubleFunction.applyAsDouble(d);
	}
	
	/** Returns a generic version of the given doubleFunction */
	public static <R> Function<Double, R> asGeneric(DoubleFunction<R> doubleFunction) {
		return (d) -> doubleFunction.apply(d);
	}
	
	/** Returns a generic version of the given doublePredicate */
	public static Predicate<Double> asGeneric(DoublePredicate doublePredicate) {
		return (d) -> doublePredicate.test(d);
	}
	
	/** Returns a generic version of the given doubleSupplier */
	public static Supplier<Double> asGeneric(DoubleSupplier doubleSupplier) {
		return () -> doubleSupplier.getAsDouble();
	}
	
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
			f.apply(arr[i]);
		}
	}
	
	/** Applies f to each element in col
	 * @param arr - an array of values.
	 * @param f - a consumer function
	 */
	public static <T> void forEach(Iterable<T> col, Consumer<T> f){
		for(T t : col){
			f.apply(t);
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
	
	/** Applies f to each element in arr, putting the results into a new list
	 * @param arr - an array of values
	 * @param f - a consumer function
	 * @return - a list of the mapped values: [f(arr[0]), f(arr[1]), ....]
	 */
	public static <T> List<T> map(Iterable<T> col, UnaryOperator<T> f){
		DeArrList<T> lst = new DeArrList<>();
		for(T t : col){
			lst.add(f.apply(t));
		}
		return lst;
	}
	
	/** Folds over the given array. Folds from left to right, with the accumulator given as the
	 * first argument and the element as the second argument.
	 * @param start - the default value to begin the folding with.
	 * @param arr - the array of values to fold over
	 * @param f - the folding function
	 * @return - the folded value. If arr is empty, returns start.
	 */
	public static <T, R> R foldLeft(R start, T[] arr, BiFunction<R, T, R> f) {
		for(T t : arr) {
			start = f.apply(start, t);
		}
		return start;
	}
	
	/** Folds over the given array. Folds from left to right, with the accumulator given as the
	 * first argument and the element as the second argument.
	 * @param start - the default value to begin the folding with.
	 * @param col - the Iterable of values to fold over
	 * @param f - the folding function
	 * @return - the folded value. If col is empty, returns start.
	 */
	public static <T, R> R foldLeft(R start, Iterable<T> col, BiFunction<R, T, R> f) {
		for(T t : col) {
			start = f.apply(start, t);
		}
		return start;
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
	
	private FunctionalUtil(){}
}
