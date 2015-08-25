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
}
