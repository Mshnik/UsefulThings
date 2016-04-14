package common.types;

import common.Util;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An abstract parent of all tuples, a simple Product Type implementation.
 * Tuples as implemented here are immutable.
 * Stores all tupled values in an Object array for basic method implementations
 * Contains default implementations of toString(), equals(..), and hashCode().
 * <p>
 * Construction of all tuples should use the static {@code of} methods declared in class Tuple.
 *
 * @author Mshnik
 */
public abstract class Tuple implements Cloneable, Iterable<Object>, Serializable {

  /** The size of the largest available tuple */
  private static final int MAX_TUPLE_SIZE = 8;

  /** Returns the empty tuple - this is the singleton instance declared in Tuple0 */
  public static Tuple0 of() {
    return Tuple0.SINGLETON_INSTANCE;
  }

  /**
   * Returns a new tuple of the given objects
   */
  public static <A> Tuple1<A> of(A a) {
    return new Tuple1<>(a);
  }

  /**
   * Returns a new tuple of the given objects
   */
  public static <A, B> Tuple2<A, B> of(A a, B b) {
    return new Tuple2<>(a, b);
  }

  /**
   * Returns a new tuple of the given objects
   */
  public static <A, B, C> Tuple3<A, B, C> of(A a, B b, C c) {
    return new Tuple3<>(a, b, c);
  }

  /**
   * Returns a new tuple of the given objects
   */
  public static <A, B, C, D> Tuple4<A, B, C, D> of(A a, B b, C c, D d) {
    return new Tuple4<>(a, b, c, d);
  }

  /**
   * Returns a new tuple of the given objects
   */
  public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(A a, B b, C c, D d, E e) {
    return new Tuple5<>(a, b, c, d, e);
  }

  /**
   * Returns a new tuple of the given objects
   */
  public static <A, B, C, D, E, F> Tuple6<A, B, C, D, E, F> of(A a, B b, C c, D d, E e, F f) {
    return new Tuple6<>(a, b, c, d, e, f);
  }

  /**
   * Returns a new tuple of the given objects
   */
  public static <A, B, C, D, E, F, G> Tuple7<A, B, C, D, E, F, G> of(A a, B b, C c, D d, E e, F f, G g) {
    return new Tuple7<>(a, b, c, d, e, f, g);
  }

  /**
   * Returns a new tuple of the given objects
   */
  public static <A, B, C, D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> of(A a, B b, C c, D d, E e, F f, G g, H h) {
    return new Tuple8<>(a, b, c, d, e, f, g, h);
  }

  /**
   * The objects stored in the tuple. Shouldn't be altered
   * after initialization. If it must be, subclass should make
   * sure to update its fields to reflect this
   */
  protected final Object[] vals;

  /**
   * Constructor for the AbsTuple class. Takes the values stored in this tuple.
   * Only implementations should be declared in this package, thus the constructor
   * has package protected visibility.
   * @param v - the values stored in this tuple
   */
  Tuple(Object... v) {
    vals = v;
  }

  /**
   * Copy constructor for tuples. Should create a shallow copy of this tuple - the objects
   * stored within the tuple are not copied.
   */
  public abstract Tuple clone();

  /**
   * Creates a new tuple that adds the value X to the end of this tuple, creating a tuple of
   * length one greater than this. The new Tuple is a shallow copy; the values are not copied.
   * @param x - the value to append
   * @param <X> - the type of the value to append.
   * @return - a new tuple, storing the values of this, followed by x.
   */
  public abstract <X> Tuple and(X x);

  /** Creates a new tuple that drops the left most value of this tuple, creating
   * a tuple of length one less than this. The new Tuple is a shallow copy; the values are copied.
   * If this is a Tuple0, returns the same Tuple0.
   */
  public abstract Tuple dropLeft();

  /** Creates a new tuple that drops the right most value of this tuple, creating
   * a tuple of length one less than this. The new Tuple is a shallow copy; the values are copied.
   * If this is a Tuple0, returns the same Tuple0.
   */
  public abstract Tuple dropRight();

  /**
   * A basic toString for all tuples. Returns a comma separated list
   * of the values stored in this tuple, with parenthesis around it
   */
  public String toString() {
    if(vals.length == 0) {
      return "()";
    }
    String s = "(";
    for (Object o : vals) {
      s += o + ",";
    }
    return s.substring(0, s.length() - 1) + ")";
  }

  /**
   * A basic equals for all tuples. Returns true iff this and o
   * are both tuples of the same length (thus same tuple implementation)
   * and store the same values
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof Tuple)) return false;
    Tuple t = (Tuple) o;
    return vals.length == t.vals.length && Arrays.deepEquals(vals, t.vals);
  }

  /**
   * A basic hashCode implementation for all tuples. Returns
   * a hash based on the values stored within this tuple, including the
   * order in which they are stored
   */
  public int hashCode() {
    return Arrays.deepHashCode(vals);
  }

  /**
   * Returns the size of this Tuple
   */
  public int size() {
    return vals.length;
  }

  /**
   * Returns an array that represents this tuple. The returned array
   * is a copy of the underlying array, so changes will not affect the tuple
   */
  public Object[] toArray() {
    return Arrays.copyOf(vals, vals.length);
  }

  /** Return a list the represents this tuple. The returned list is an immutable copy */
  public List<Object> toList() {
    return Collections.unmodifiableList(Arrays.asList(vals));
  }

  /** Returns an Iterator over the objects in this Tuple. */
  public Iterator<Object> iterator() {
    return Util.arrIterator(toArray());
  }

  /** Return true iff this tuple contains the given value */
  public boolean contains(Object o) {
    for(Object ob : vals) {
      if(Objects.equals(o, ob)) return true;
    }
    return false;
  }

  /** Return true iff this tuple contains all of the values in the given collection */
  public boolean containsAll(Iterable<?> col) {
    for(Object o : col) {
      if(! contains(o)) return false;
    }
    return true;
  }

  /**
   * Zips lst and lst2 together. If either list is longer, the extra
   * elements are in tuples with null values.
   *
   * @param s1  - the first stream to zip
   * @param s2 - the second stream to zip
   * @return - a zipped stream of tuples.
   */
  public static <T, U> Stream<Tuple2<T, U>> zip(Stream<T> s1, Stream<U> s2) {
    Spliterator<T> aSpliterator = Objects.requireNonNull(s1).spliterator();
    Spliterator<U> bSpliterator = Objects.requireNonNull(s2).spliterator();

    // Zipping looses DISTINCT and SORTED characteristics
    int characteristics = aSpliterator.characteristics() & bSpliterator.characteristics() &
        ~(Spliterator.DISTINCT | Spliterator.SORTED);

    long zipSize = ((characteristics & Spliterator.SIZED) != 0)
        ? Math.min(aSpliterator.getExactSizeIfKnown(), bSpliterator.getExactSizeIfKnown())
        : -1;

    Iterator<T> aIterator = Spliterators.iterator(aSpliterator);
    Iterator<U> bIterator = Spliterators.iterator(bSpliterator);
    Iterator<Tuple2<T,U>> cIterator = new Iterator<Tuple2<T,U>>() {
      @Override
      public boolean hasNext() {
        return aIterator.hasNext() || bIterator.hasNext();
      }

      @Override
      public Tuple2<T,U> next() {
        T t = aIterator.hasNext() ? aIterator.next() : null;
        U u = bIterator.hasNext() ? bIterator.next() : null;
        return of(t, u);
      }
    };

    Spliterator<Tuple2<T,U>> split = Spliterators.spliterator(cIterator, zipSize, characteristics);
    return (s1.isParallel() || s2.isParallel()) ? StreamSupport.stream(split, true) : StreamSupport.stream(split, false);
  }
}
