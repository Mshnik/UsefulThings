package common.types;

/**
 * A tuple of one value, of types A
 */
public class Tuple1<A> extends Tuple {

  /**
   * The first value stored within this tuple
   */
  public final A _1;

  /**
   * Constructs a new tuple of the value (first)
   */
  protected Tuple1(A first) {
    super(first);
    _1 = first;
  }

  public Tuple1<A> clone() {
    return new Tuple1<>(_1);
  }

  /**
   * Returns a new Tuple2 that consists of appending the given B to this Tuple1
   */
  public <B> Tuple2<A,B> and(B b) {
    return Tuple.of(_1, b);
  }
}
