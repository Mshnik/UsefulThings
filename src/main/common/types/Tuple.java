package common.types;

import common.dataStructures.DeArrList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
public abstract class Tuple implements Cloneable {

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
   * Constructor for the AbsTuple class. Takes the values stored in this tuple
   *
   * @param v - the values stored in this tuple
   */
  public Tuple(Object... v) {
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

  /**
   * A basic toString for all tuples. Returns a comma separated list
   * of the values stored in this tuple, with parenthesis around it
   */
  public String toString() {
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

  /**
   * Zips lst and lst2 together. If either list is longer, the extra
   * elements are in tuples with null values.
   *
   * @param lst  - the first list to zip
   * @param lst2 - the second list to zip
   * @return - a zipped list of tuples. Some tuples may have a null value
   * but no tuple will have both null values.
   */
  public static <T, U> List<Tuple2<T, U>> zip(List<T> lst, List<U> lst2) {
    DeArrList<Tuple2<T, U>> zLst = new DeArrList<>();
    Iterator<T> i = lst.iterator();
    Iterator<U> i2 = lst2.iterator();
    while (i.hasNext() && i2.hasNext()) {
      zLst.add(Tuple.of(i.next(), i2.next()));
    }
    while (i.hasNext()) {
      zLst.add(Tuple.of(i.next(), null));
    }
    while (i2.hasNext()) {
      zLst.add(Tuple.of(null, i2.next()));
    }
    return zLst;
  }

}
