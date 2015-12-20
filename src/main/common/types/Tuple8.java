package common.types;

/**
 * A tuple of eight values, of types A, B, C, D, E, F, G and H, respectively.
 */
public class Tuple8<A, B, C, D, E, F, G, H> extends Tuple {

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
   * The sixth value stored within this tuple
   */
  public final F _6;

  /**
   * The seventh value stored within this tuple
   */
  public final G _7;

  /**
   * The eighth value stored within this tuple
   */
  public final H _8;

  /**
   * Constructs a new tuple of the values (first, second, third, fourth,
   * fifth, sixth, seventh, eighth)
   */
  protected Tuple8(A first, B second, C third, D fourth, E fifth,
                   F sixth, G seventh, H eighth) {
    super(first, second, third, fourth, fifth, sixth, seventh, eighth);
    _1 = first;
    _2 = second;
    _3 = third;
    _4 = fourth;
    _5 = fifth;
    _6 = sixth;
    _7 = seventh;
    _8 = eighth;
  }

  public Tuple8<A, B, C, D, E, F, G, H> clone() {
    return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
  }

  public <X> Tuple and(X x) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Can't make tuples greater than size eight (at this time)");
  }

  @Override
  public Tuple7<B,C,D,E,F,G,H> dropLeft() {
    return Tuple.of(_2,_3,_4,_5,_6,_7,_8);
  }

  @Override
  public Tuple7<A,B,C,D,E,F,G> dropRight() {
    return Tuple.of(_1,_2,_3,_4,_5,_6,_7);
  }
}
