package functional;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.*;

import common.dataStructures.DeArrList;
import common.types.Tuple;
import common.types.Tuple2;

public class FunctionalUtil {
	
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
	public static void forEach(int[] arr, Consumer<Integer> f){
		for(int i = 0; i < arr.length; i++){
			f.apply(arr[i]);
		}
	}
	
	/** Applies f to each element in arr
	 * @param arr - an array of values.
	 * @param f - a consumer function
	 */
	public static void forEach(double[] arr, Consumer<Double> f){
		for(int i = 0; i < arr.length; i++){
			f.apply(arr[i]);
		}
	}
	
	/** Applies f to each element in arr
	 * @param arr - an array of values.
	 * @param f - a consumer function
	 */
	public static void forEach(long[] arr, Consumer<Long> f){
		for(int i = 0; i < arr.length; i++){
			f.apply(arr[i]);
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
	
	/** Applies f to each element in arr, putting the results into a new list
	 * @param arr - an array of values
	 * @param f - a function
	 * @return - a list of the mapped values: [f(arr[0]), f(arr[1]), ....]
	 */
	public static <T,S> List<S> map(T[] arr, Function<T,S> f){
		DeArrList<S> lst = new DeArrList<>();
		for(T t : arr){
			lst.add(f.apply(t));
		}
		return lst;
	}
	
	/** Applies f to each element in arr, putting the results into a new list
	 * @param col - a collection of values
	 * @param f - a function
	 * @return - a list of the mapped values: [f(arr[0]), f(arr[1]), ....]
	 */
	public static <T,S> List<S> map(Iterable<T> col, Function<T,S> f){
		DeArrList<S> lst = new DeArrList<>();
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
		return foldLeft(start, Arrays.asList(arr), f);
	}
	
	/** Folds over the given collection. Folds from left to right, with the accumulator given as the
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
	
	/** Folds over the given array. Folds from left to right, with the accumulator given as the
	 * first argument and the element as the second argument.
	 * @param start - the default value to begin the folding with.
	 * @param arr - the array of values to fold over
	 * @param f - the folding function
	 * @return - the folded value. If arr is empty, returns start.
	 */
	public static <T, S, R> R foldLeft2(R start, T[] arr, S[] arr2, TriFunction<R, T, S, R> f) {
		int min = Math.min(arr.length, arr2.length);
		for(int i = 0; i < min; i++) {
			start = f.apply(start, arr[i], arr2[i]);
		}
		return start;
	}
	
	/** Folds over the given collection. Folds from left to right, with the accumulator given as the
	 * first argument and the element as the second argument.
	 * @param start - the default value to begin the folding with.
	 * @param col - the Iterable of values to fold over
	 * @param f - the folding function
	 * @return - the folded value. If col is empty, returns start.
	 */
	public static <T, S, R> R foldLeft2(R start, Iterable<T> col, Iterable<S> col2, TriFunction<R, T, S, R> f) {
		Iterator<T> c = col.iterator();
		Iterator<S> c2 = col2.iterator();
		while(c.hasNext() && c2.hasNext()) {
			start = f.apply(start, c.next(), c2.next());
		}
		return start;
	}
	
	/** Constructs a new list with elements filtered by the given predicate */
	public static <T> List<T> filter(T[] col, Predicate<T> f) {
		return filter(Arrays.asList(col), f);
	}
	
	/** Constructs a new list with elements filtered by the given predicate */
	public static <T> List<T> filter(Iterable<T> col, Predicate<T> f) {
		List<T> lst = new DeArrList<>();
		for(T t : col) {
			if(f.apply(t)) {
				lst.add(t);
			}
		}
		return lst;
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
			zLst.add(Tuple.of(i.next(), i2.next()));
		}
		while(i.hasNext()){
			zLst.add(Tuple.of(i.next(), null));
		}
		while(i2.hasNext()){
			zLst.add(Tuple.of(null, i2.next()));
		}
		return zLst;
	}
	
	private FunctionalUtil(){}
}
