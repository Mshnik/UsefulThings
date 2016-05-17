package common.math;

import static common.math.NumExt.wrap;

/**
 * @author Mshnik
 */
public class Rational<T extends Number> extends Number {

  private final T numerator;
  private final T denominator;

  public Rational(T numerator, T denominator) throws IllegalArgumentException {
    this.numerator = numerator;
    this.denominator = denominator;
    if (wrap(denominator).isZero()) {
      throw new IllegalArgumentException("Zero denominator");
    }
  }

  public Rational<T> invert() {
    return new Rational<>(denominator, numerator);
  }

  @Override
  public int intValue() {
    return wrap(numerator).divide(denominator).intValue();
  }

  @Override
  public long longValue() {
    return wrap(numerator).divide(denominator).longValue();
  }

  @Override
  public float floatValue() {
    return wrap(numerator).divide(denominator).floatValue();
  }

  @Override
  public double doubleValue() {
    return wrap(numerator).divide(denominator).doubleValue();
  }
}
