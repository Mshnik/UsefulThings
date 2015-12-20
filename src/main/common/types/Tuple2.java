package common.types;

/**
 * A tuple of two values, of types A and B, respectively
 */
public class Tuple2<A, B> extends Tuple {

  /**
   * The first value stored within this tuple
   */
  public final A _1;

  /**
   * The second value stored within this tuple
   */
  public final B _2;

  /**
   * Constructs a new tuple of the values (first, second)
   */
  protected Tuple2(A first, B second) {
    super(first, second);
    _1 = first;
    _2 = second;
  }

  public Tuple2<A, B> clone() {
    return new Tuple2<>(_1, _2);
  }

  /**
   * Returns a new Tuple3 that consists of appending the given C to this Tuple2
   */
  public <C> Tuple3<A, B, C> and(C c) {
    return Tuple.of(_1, _2, c);
  }

  public Tuple1<A> dropRight() {
    return Tuple.of(_1);
  }

  public Tuple1<B> dropLeft() {
    return Tuple.of(_2);
  }
}
