package common.math;

import common.types.Tuple;
import common.types.Tuple2;

import static common.math.NumExt.*;

/**
 * @author Mshnik
 */
public class Rational extends Number implements Comparable<Rational>{

  public static final Rational ZERO = Rational.of(0, 1);
  public static final Rational ONE = Rational.of(1,1);
  public static final Rational NEG_ONE = Rational.of(-1,1);

  private final NumExt numWrap;
  private final NumExt denomWrap;

  private Rational(NumExt numerator, NumExt denominator)  {
    this.numWrap = numerator;
    this.denomWrap = denominator;
  }

  public static Rational of(Number numerator, Number denominator) throws IllegalArgumentException{
    NumExt numWrap = wrap(numerator);
    NumExt denomWrap = wrap(denominator);

    //Check for zero, make sure sign is in numerator
    if (denomWrap.isZero()) {
      throw new IllegalArgumentException("Zero denominator");
    } else if (denomWrap.signum() < 0) {
      numWrap = numWrap.negate();
      denomWrap = denomWrap.negate();
    }

    //Get numerator and denominator into lowest terms
    //TODO

    return new Rational(numWrap,denomWrap);
  }

  public static Rational of(Number n) {
    return of(n, 1);
  }

  public Rational negate() {
    return Rational.of(numWrap.negate(), denomWrap);
  }

  public boolean isZero() {
    return numWrap.isZero();
  }

  public int signum() {
    return numWrap.signum();
  }

  public Rational invert() {
    return Rational.of(denomWrap, numWrap);
  }

  public Rational add(Rational r) {
    return of(numWrap.multiply(r.denomWrap).add(r.numWrap.multiply(denomWrap)), denomWrap.multiply(r.denomWrap));
  }

  public Rational multiply(Rational r) {
    return of(numWrap.multiply(r.numWrap), denomWrap.multiply(r.denomWrap));
  }

  public Rational subtract(Rational r) {
    return of(numWrap.multiply(r.denomWrap).subtract(r.numWrap.multiply(denomWrap)), denomWrap.multiply(r.denomWrap));
  }

  public Rational divide(Rational r) {
    return of(numWrap.multiply(r.denomWrap), denomWrap.multiply(r.numWrap));
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (! (o instanceof Rational)) return false;

    Rational r = (Rational)o;
    return r.numWrap.equals(numWrap) && r.denomWrap.equals(denomWrap);
  }

  public int hashCode() {
    return numWrap.hashCode() + (denomWrap.hashCode() << 16);
  }

  @Override
  public int compareTo(Rational o) {
    return subtract(o).signum();
  }

  @Override
  public int intValue() {
    return numWrap.divide(denomWrap).intValue();
  }

  @Override
  public long longValue() {
    return numWrap.divide(denomWrap).longValue();
  }

  @Override
  public float floatValue() {
    return numWrap.divide(denomWrap).floatValue();
  }

  @Override
  public double doubleValue() {
    return numWrap.divide(denomWrap).doubleValue();
  }

  public String toString() {
    return numWrap.toString() + "/" + denomWrap.toString();
  }
}
