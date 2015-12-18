package common.types;

/**
 * One half of the Sum Type implementation. Every Right instance
 * will wrap an object of type B.
 *
 * @param <A> The unused type
 * @param <B> The type of objects to wrap in this Left.
 * @author Mshnik
 */
class Right<A, B> extends Either<A, B> {

  private B val;

  /**
   * Constructs a new Right wrapping the given B
   */
  Right(B b) throws IllegalArgumentException {
    super(false);
    val = b;
  }

  /**
   * Returns the B wrapped in this Right
   */
  public B getVal() {
    return val;
  }

  /**
   * Returns the type B. Returns null if this was wrapping null.
   */
  @SuppressWarnings("unchecked")
  public Class<B> getType() {
    return val == null ? null : (Class<B>) val.getClass();
  }

  /**
   * Wraps the Either default toString implementation in Right
   */
  @Override
  public String toString() {
    return "Right(" + val + ")";
  }
}
