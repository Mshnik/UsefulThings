package common.types;

/**
 * A tuple of four values, of types A, B, C and D, respectively
 */
public class Tuple4<A, B, C, D> extends Tuple {

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
   * The fourth value stored within this tuple
   */
  public final D _4;

  /**
   * Constructs a new tuple of the values (first, second, third, fourth)
   */
  protected Tuple4(A first, B second, C third, D fourth) {
    super(first, second, third, fourth);
    _1 = first;
    _2 = second;
    _3 = third;
    _4 = fourth;
  }

  public Tuple4<A, B, C, D> clone() {
    return new Tuple4<>(_1, _2, _3, _4);
  }

  /**
   * Returns a new Tuple5 that consists of appending the given E to this Tuple5
   */
  public <E> Tuple5<A, B, C, D, E> and(E e) {
    return Tuple.of(_1, _2, _3, _4, e);
  }

  public Tuple3<A,B,C> dropRight() {
    return Tuple.of(_1,_2,_3);
  }

  public Tuple3<B,C,D> dropLeft() {
    return Tuple.of(_2,_3,_4);
  }
}
