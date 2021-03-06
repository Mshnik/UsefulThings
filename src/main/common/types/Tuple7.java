package common.types;

/**
 * A tuple of seven values, of types A, B, C, D, E, F and G, respectively
 */
public class Tuple7<A, B, C, D, E, F, G> extends Tuple {

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
   * Constructs a new tuple of the values (first, second, third, fourth,
   * fifth, sixth, seventh)
   */
  protected Tuple7(A first, B second, C third, D fourth, E fifth, F sixth, G seventh) {
    super(first, second, third, fourth, fifth, sixth, seventh);
    _1 = first;
    _2 = second;
    _3 = third;
    _4 = fourth;
    _5 = fifth;
    _6 = sixth;
    _7 = seventh;
  }

  public Tuple7<A, B, C, D, E, F, G> clone() {
    return new Tuple7<>(_1, _2, _3, _4, _5, _6, _7);
  }

  /**
   * Returns a new Tuple8 that consists of appending the given H to this Tuple8
   */
  public <H> Tuple8<A, B, C, D, E, F, G, H> and(H h) {
    return Tuple.of(_1, _2, _3, _4, _5, _6, _7, h);
  }

  @Override
  public Tuple6<B,C,D,E,F,G> dropLeft() {
    return Tuple.of(_2, _3, _4, _5, _6, _7);
  }

  @Override
  public Tuple6<A,B,C,D,E,F> dropRight() {
    return Tuple.of(_1, _2, _3, _4, _5, _6);
  }
}
