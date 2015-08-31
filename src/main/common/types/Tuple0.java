package common.types;

/**
 * A tuple of zero values
 */
public class Tuple0 extends Tuple {

  protected Tuple0() {
    super();
  }

  @Override
  public Tuple clone() {
    return new Tuple0();
  }

  @Override
  public <X> Tuple and(X x) {
    return new Tuple1<>(x);
  }
}
