package common.types;

/**
 * One half of the Sum Type implementation. Every Left instance
 * will wrap an object of type A.
 *
 * @param <A> The type of objects to wrap in this Left.
 * @param <B> The unused type.
 * @author Mshnik
 */
public class Left<A, B> extends Either<A, B> {

  private A val;

  /**
   * Constructs a new Left wrapping the given A
   */
  public Left(A a) throws IllegalArgumentException {
    super(true);
    if (a == null) throw new IllegalArgumentException("Can't Wrap null");
    val = a;
  }

  /**
   * Returns the A wrapped in this Left
   */
  public A getVal() {
    return val;
  }

  /**
   * Returns the type A
   */
  @SuppressWarnings("unchecked")
  public Class<A> getType() {
    return (Class<A>) val.getClass();
  }

  /**
   * Wraps the Either default toString implementation in Left
   */
  @Override
  public String toString() {
    return "Left(" + val + ")";
  }

}
