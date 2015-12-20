package common.types;

/**
 * A tuple of zero values.
 * Because Tuple0 contains no values (and all Tuples are immutable),
 * a single Tuple0 instance can be used for all Tuple0 references.
 */
public class Tuple0 extends Tuple {

  static Tuple0 SINGLETON_INSTANCE = new Tuple0();

  private Tuple0() {
    super();
  }

  @Override
  public Tuple clone() {
    return SINGLETON_INSTANCE;
  }

  @Override
  public <X> Tuple and(X x) {
    return new Tuple1<>(x);
  }

  public Tuple0 dropLeft() {
    return this;
  }

  public Tuple0 dropRight() {
    return this;
  }
}
