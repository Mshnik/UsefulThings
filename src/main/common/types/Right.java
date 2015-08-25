package common.types;

/**
 * One half of the Sum Type implementation. Every Right instance
 * will wrap an object of type B.
 *
 * @param <A> The unused type
 * @param <B> The type of objects to wrap in this Left.
 * @author Mshnik
 */
public class Right<A, B> extends Either<A, B> {

  private B val;

  /**
   * Constructs a new Right wrapping the given B
   */
  public Right(B b) throws IllegalArgumentException {
    super(false);
    if (b == null) throw new IllegalArgumentException("Can't Wrap null");
    val = b;
  }

  /**
   * Returns the B wrapped in this Right
   */
  public B getVal() {
    return val;
  }

  /**
   * Returns the type B
   */
  @SuppressWarnings("unchecked")
  public Class<B> getType() {
    return (Class<B>) val.getClass();
  }

  /**
   * Wraps the Either default toString implementation in Right
   */
  @Override
  public String toString() {
    return "Right(" + val + ")";
  }
}
