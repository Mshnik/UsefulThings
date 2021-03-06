package common.types;

/**
 * A tuple of three values, of types A, B and C, respectively
 */
public class Tuple3<A, B, C> extends Tuple {

  /**
   * The first value stored within this tuple
   */
  public final A _1;

  /**
   * The second value stored within this tuple
   */
  public final B _2;

  /**
   * The third value stored within this tuple
   */
  public final C _3;

  /**
   * Constructs a new tuple of the values (first, second, third)
   */
  protected Tuple3(A first, B second, C third) {
    super(first, second, third);
    _1 = first;
    _2 = second;
    _3 = third;
  }

  public Tuple3<A, B, C> clone() {
    return new Tuple3<>(_1, _2, _3);
  }

  /**
   * Returns a new Tuple4 that consists of appending the given D to this Tuple4
   */
  public <D> Tuple4<A, B, C, D> and(D d) {
    return Tuple.of(_1, _2, _3, d);
  }

  public Tuple2<A,B> dropRight() {
    return Tuple.of(_1, _2);
  }

  public Tuple2<B,C> dropLeft() {
    return Tuple.of(_2, _3);
  }
}
