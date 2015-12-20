package common.types;

/**
 * A tuple of six values, of types A, B, C, D, E and F, respectively
 */
public class Tuple6<A, B, C, D, E, F> extends Tuple {

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
   * Constructs a new tuple of the values (first, second, third, fourth, fifth, sixth)
   */
  protected Tuple6(A first, B second, C third, D fourth, E fifth, F sixth) {
    super(first, second, third, fourth, fifth, sixth);
    _1 = first;
    _2 = second;
    _3 = third;
    _4 = fourth;
    _5 = fifth;
    _6 = sixth;
  }

  public Tuple6<A, B, C, D, E, F> clone() {
    return new Tuple6<>(_1, _2, _3, _4, _5, _6);
  }

  /**
   * Returns a new Tuple6 that consists of appending the given E to this Tuple6
   */
  public <G> Tuple7<A, B, C, D, E, F, G> and(G g) {
    return Tuple.of(_1, _2, _3, _4, _5, _6, g);
  }

  @Override
  public Tuple5<B,C,D,E,F> dropLeft() {
    return Tuple.of(_2,_3,_4,_5,_6);
  }

  @Override
  public Tuple5<A,B,C,D,E> dropRight() {
    return Tuple.of(_1,_2,_3,_4,_5);
  }
}
