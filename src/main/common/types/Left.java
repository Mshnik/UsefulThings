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
    val = a;
  }

  /**
   * Returns the A wrapped in this Left
   */
  public A getVal() {
    return val;
  }

  /**
   * Returns the type A. Returns null if this was wrapping null.
   */
  @SuppressWarnings("unchecked")
  public Class<A> getType() {
    return val == null ? null : (Class<A>) val.getClass();
  }

  /**
   * Wraps the Either default toString implementation in Left
   */
  @Override
  public String toString() {
    return "Left(" + val + ")";
  }

}
