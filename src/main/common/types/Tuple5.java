package common.types;

/**
 * A tuple of five values, of types A, B, C, D and E, respectively
 */
public class Tuple5<A, B, C, D, E> extends Tuple {

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
   * The fifth value stored within this tuple
   */
  public final E _5;

  /**
   * Constructs a new tuple of the values (first, second, third, fourth)
   */
  protected Tuple5(A first, B second, C third, D fourth, E fifth) {
    super(first, second, third, fourth, fifth);
    _1 = first;
    _2 = second;
    _3 = third;
    _4 = fourth;
    _5 = fifth;
  }

  public Tuple5<A, B, C, D, E> clone() {
    return new Tuple5<>(_1, _2, _3, _4, _5);
  }

  @Override
  public <F> Tuple and(F f) {
    return new Tuple6<>(_1, _2, _3, _4, _5, f);
  }

  @Override
  public Tuple4<B,C,D,E> dropLeft() {
    return Tuple.of(_2,_3,_4,_5);
  }

  @Override
  public Tuple4<A,B,C,D> dropRight() {
    return Tuple.of(_1,_2,_3,_4);
  }

}
