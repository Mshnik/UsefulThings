package functional.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import common.dataStructures.DeArrList;
import functional.impl.*;

public class FunctionalUtil {
  /**
   * Returns a UsefulThings version of the given Consumer1
   */
  public static <A> Consumer1<A> migrate(java.util.function.Consumer<A> consumer) {
    return consumer::accept;
  }

  /**
   * Returns a UsefulThings version of the given Supplier
   */
  public static <A> Supplier<A> migrate(java.util.function.Supplier<A> supplier) {
    return supplier::get;
  }

  /**
   * Returns a UsefulThings version of the given Predicate1
   */
  public static <A> Predicate1<A> migrate(java.util.function.Predicate<A> predicate) {
    return predicate::test;
  }

  /**
   * Returns a UsefulThings version of the given Function1
   */
  public static <A,R> Function1<A,R> migrate(java.util.function.Function<A,R> function) {
    return function::apply;
  }

  /**
   * Returns a UsefulThings version of the given Consumer2
   */
  public static <A,B> Consumer2<A,B> migrate(java.util.function.BiConsumer<A,B> biConsumer) {
    return biConsumer::accept;
  }

  /**
   * Returns a UsefulThings version of the given Predicate2
   */
  public static <A,B> Predicate2<A,B> migrate(java.util.function.BiPredicate<A,B> biPredicate) {
    return biPredicate::test;
  }

  /**
   * Returns a UsefulThings version of the given Function2
   */
  public static <A,B,R> Function2<A,B,R> migrate(java.util.function.BiFunction<A,B,R> biFunction) {
    return biFunction::apply;
  }

  /**
   * Applies f to each element in arr
   *
   * @param arr - an array of values.
   * @param f   - a consumer function
   */
  public static void forEach(int[] arr, Consumer1<Integer> f) {
    for (int i = 0; i < arr.length; i++) {
      f.apply(arr[i]);
    }
  }

  /**
   * Applies f to each element in arr
   *
   * @param arr - an array of values.
   * @param f   - a consumer function
   */
  public static void forEach(double[] arr, Consumer1<Double> f) {
    for (int i = 0; i < arr.length; i++) {
      f.apply(arr[i]);
    }
  }

  /**
   * Applies f to each element in arr
   *
   * @param arr - an array of values.
   * @param f   - a consumer function
   */
  public static void forEach(long[] arr, Consumer1<Long> f) {
    for (int i = 0; i < arr.length; i++) {
      f.apply(arr[i]);
    }
  }

  /**
   * Applies f to each element in arr
   *
   * @param arr - an array of values.
   * @param f   - a consumer function
   */
  public static <T> void forEach(T[] arr, Consumer1<T> f) {
    for (int i = 0; i < arr.length; i++) {
      f.apply(arr[i]);
    }
  }

  /**
   * Applies f to each element in col
   *
   * @param col - an iterable of values.
   * @param f   - a consumer function
   */
  public static <T> void forEach(Iterable<T> col, Consumer1<T> f) {
    for (T t : col) {
      f.apply(t);
    }
  }

  /**
   * Applies f to each element in arr, putting the results into a new list
   *
   * @param arr - an array of values
   * @param f   - a function
   * @return - a list of the mapped values: [f(arr[0]), f(arr[1]), ....]
   */
  public static <T, S> List<S> map(T[] arr, Function1<T, S> f) {
    DeArrList<S> lst = new DeArrList<>();
    for (T t : arr) {
      lst.add(f.apply(t));
    }
    return lst;
  }

  /**
   * Applies f to each element in arr, putting the results into a new list
   *
   * @param col - a collection of values
   * @param f   - a function
   * @return - a list of the mapped values: [f(arr[0]), f(arr[1]), ....]
   */
  public static <T, S> List<S> map(Iterable<T> col, Function1<T, S> f) {
    DeArrList<S> lst = new DeArrList<>();
    for (T t : col) {
      lst.add(f.apply(t));
    }
    return lst;
  }

  /**
   * Folds over the given array. Folds from left to right, with the accumulator given as the
   * first argument and the element as the second argument.
   *
   * @param start - the default value to begin the folding with.
   * @param arr   - the array of values to fold over
   * @param f     - the folding function
   * @return - the folded value. If arr is empty, returns start.
   */
  public static <T, R> R foldLeft(R start, T[] arr, Function2<R, T, R> f) {
    return foldLeft(start, Arrays.asList(arr), f);
  }

  /**
   * Folds over the given collection. Folds from left to right, with the accumulator given as the
   * first argument and the element as the second argument.
   *
   * @param start - the default value to begin the folding with.
   * @param col   - the Iterable of values to fold over
   * @param f     - the folding function
   * @return - the folded value. If col is empty, returns start.
   */
  public static <T, R> R foldLeft(R start, Iterable<T> col, Function2<R, T, R> f) {
    for (T t : col) {
      start = f.apply(start, t);
    }
    return start;
  }

  /**
   * Folds over the given array. Folds from left to right, with the accumulator given as the
   * first argument and the element as the second argument.
   *
   * @param start - the default value to begin the folding with.
   * @param arr   - the array of values to fold over
   * @param f     - the folding function
   * @return - the folded value. If arr is empty, returns start.
   */
  public static <T, S, R> R foldLeft2(R start, T[] arr, S[] arr2, Function3<R, T, S, R> f) {
    int min = Math.min(arr.length, arr2.length);
    for (int i = 0; i < min; i++) {
      start = f.apply(start, arr[i], arr2[i]);
    }
    return start;
  }

  /**
   * Folds over the given collection. Folds from left to right, with the accumulator given as the
   * first argument and the element as the second argument.
   *
   * @param start - the default value to begin the folding with.
   * @param col   - the Iterable of values to fold over
   * @param f     - the folding function
   * @return - the folded value. If col is empty, returns start.
   */
  public static <T, S, R> R foldLeft2(R start, Iterable<T> col, Iterable<S> col2, Function3<R, T, S, R> f) {
    Iterator<T> c = col.iterator();
    Iterator<S> c2 = col2.iterator();
    while (c.hasNext() && c2.hasNext()) {
      start = f.apply(start, c.next(), c2.next());
    }
    return start;
  }

  /**
   * Constructs a new list with elements filtered by the given predicate
   */
  public static <T> List<T> filter(T[] col, Predicate1<T> f) {
    return filter(Arrays.asList(col), f);
  }

  /**
   * Constructs a new list with elements filtered by the given predicate
   */
  public static <T> List<T> filter(Iterable<T> col, Predicate1<T> f) {
    List<T> lst = new DeArrList<>();
    for (T t : col) {
      if (f.apply(t)) {
        lst.add(t);
      }
    }
    return lst;
  }

  private FunctionalUtil() {
  }
}
