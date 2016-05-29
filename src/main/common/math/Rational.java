package common.math;

import common.types.Tuple;
import common.types.Tuple2;

/**
 * @author Mshnik
 */
public class Rational extends NumExt implements Comparable<Number>{

  public static final Rational ZERO = new Rational(NumExt.ZERO, NumExt.ONE);
  public static final Rational ONE = new Rational(NumExt.ONE, NumExt.ONE);
  public static final Rational NEG_ONE = new Rational(NumExt.NEG_ONE, NumExt.ONE);

  private final NumExt numWrap;
  private final NumExt denomWrap;

  //region Construction
  //-----------------------------------------------------------------------------------------------


  private Rational(NumExt numerator, NumExt denominator)  {
    this.numWrap = numerator;
    this.denomWrap = denominator;
  }

  public static Rational wrap(Number numerator, Number denominator) throws ArithmeticException{
    NumExt numWrap = NumExt.wrap(numerator);
    NumExt denomWrap = NumExt.wrap(denominator);

    //Check for numerator zero
    if (numWrap.equals(NumExt.ZERO)) {
      return ZERO;
    }

    //Check for illegal zero, make sure sign is in numerator
    if (denomWrap.isZero()) {
      throw new ArithmeticException("Zero denominator");
    } else if (denomWrap.signum() < 0) {
      numWrap = numWrap.negate();
      denomWrap = denomWrap.negate();
    }

    //Get numerator and denominator into lowest terms
    NumExt gcd = numWrap.gcd(denomWrap);
    numWrap = numWrap.divide(gcd);
    denomWrap = denomWrap.divide(gcd);

    //Check if we can return a constant instead of creating new Rational
    if (denomWrap.equals(NumExt.ONE) && numWrap.equals(NumExt.ONE)) {
      return ONE;
    } else if (denomWrap.equals(NumExt.ONE) && numWrap.equals(NumExt.NEG_ONE)) {
      return NEG_ONE;
    } else {
      return new Rational(numWrap,denomWrap);
    }
  }

  public static Rational wrap(Number n) {
    return NumExt.applyByNumType(n, x -> wrap(n, 1),x -> wrap(n, 1),x -> wrap(n, 1),x -> wrap(n, 1),
                                    x -> wrap(n, 1),x -> wrap(n, 1),x -> wrap(n, 1), x->x, x->wrap(n,1));
  }

  public static Rational wrap(Tuple2<? extends Number, ? extends Number> t) {
    return wrap(t._1, t._2);
  }

  //-----------------------------------------------------------------------------------------------
  //endregion

  //region Getters
  //-----------------------------------------------------------------------------------------------

  public NumExt getNumerator() {
    return numWrap;
  }

  public NumExt getDenominator() {
    return denomWrap;
  }

  @Override
  public Number getVal() {
    return doubleValue();
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
    return numWrap.asFloat().divide(denomWrap).floatValue();
  }

  @Override
  public double doubleValue() {
    return numWrap.asDouble().divide(denomWrap).doubleValue();
  }

  //-----------------------------------------------------------------------------------------------
  //endregion

  //region Mutators

  public Rational roundUp() {
    NumExt m = numWrap.mod(denomWrap);
    if (m.equals(NumExt.ZERO)) return this;
    if (signum() > 0) {
      return wrap(numWrap.subtract(m).add(denomWrap), denomWrap);
    } else {
      return wrap(numWrap.add(m), denomWrap);
    }
  }

  public Rational roundDown() {
    NumExt m = numWrap.mod(denomWrap);
    if (m.equals(NumExt.ZERO)) return this;
    if (signum() > 0) {
      return wrap(numWrap.subtract(m), denomWrap);
    } else {
      return wrap(numWrap.add(m).subtract(denomWrap), denomWrap);
    }
  }

  public Rational round() {
    NumExt m = numWrap.mod(denomWrap);
    NumExt d = denomWrap.divide(NumExt.TWO);
    int s = signum();
    if (m.equals(NumExt.ZERO)) return this;
    if (s > 0 && m.gte(d)) {
      return wrap(numWrap.subtract(m).add(denomWrap), denomWrap);
    } else if (s > 0 && m.lt(d)) {
      return wrap(numWrap.subtract(m), denomWrap);
    } else if (s < 0 && m.lte(d)) {
      return wrap(numWrap.add(m).subtract(denomWrap), denomWrap);
    } else {
      return wrap(numWrap.add(m), denomWrap);
    }
  }

  //endregion

  //region Arithmetic
  //-----------------------------------------------------------------------------------------------

  public Rational invert() {
    return Rational.wrap(denomWrap, numWrap);
  }

  public Rational negate() {
    return Rational.wrap(numWrap.negate(), denomWrap);
  }

  public int signum() {
    return numWrap.signum();
  }

  public Rational abs() {
    return wrap(numWrap.abs(), denomWrap);
  }

  public Rational add(Rational r) {
    return wrap(numWrap.multiply(r.denomWrap).add(r.numWrap.multiply(denomWrap)), denomWrap.multiply(r.denomWrap));
  }

  public Rational multiply(Rational r) {
    return wrap(numWrap.multiply(r.numWrap), denomWrap.multiply(r.denomWrap));
  }

  public Rational subtract(Rational r) {
    return wrap(numWrap.multiply(r.denomWrap).subtract(r.numWrap.multiply(denomWrap)), denomWrap.multiply(r.denomWrap));
  }

  public Rational divide(Rational r) {
    return wrap(numWrap.multiply(r.denomWrap), denomWrap.multiply(r.numWrap));
  }

  @Override
  public Rational add(Byte t2) {
    return add(Rational.wrap(t2,1));
  }

  @Override
  public Rational subtract(Byte t2) {
    return subtract(Rational.wrap(t2,1));
  }

  @Override
  public Rational multiply(Byte t2) {
    return multiply(Rational.wrap(t2,1));
  }

  @Override
  public Rational divide(Byte t2) {
    return divide(Rational.wrap(t2,1));
  }

  @Override
  public Rational add(Short t2) {
    return add(Rational.wrap(t2,1));
  }

  @Override
  public Rational subtract(Short t2) {
    return subtract(Rational.wrap(t2,1));
  }

  @Override
  public Rational multiply(Short t2) {
    return multiply(Rational.wrap(t2,1));
  }

  @Override
  public Rational divide(Short t2) {
    return divide(Rational.wrap(t2,1));
  }

  @Override
  public Rational add(Integer t2) {
    return add(Rational.wrap(t2,1));
  }

  @Override
  public Rational subtract(Integer t2) {
    return subtract(Rational.wrap(t2,1));
  }

  @Override
  public Rational multiply(Integer t2) {
    return multiply(Rational.wrap(t2,1));
  }

  @Override
  public Rational divide(Integer t2) {
    return divide(Rational.wrap(t2,1));
  }

  @Override
  public Rational add(Long t2) {
    return add(Rational.wrap(t2,1));
  }

  @Override
  public Rational subtract(Long t2) {
    return subtract(Rational.wrap(t2,1));
  }

  @Override
  public Rational multiply(Long t2) {
    return multiply(Rational.wrap(t2,1));
  }

  @Override
  public Rational divide(Long t2) {
    return divide(Rational.wrap(t2,1));
  }

  @Override
  public Rational add(Float t2) {
    return add(Rational.wrap(t2,1));
  }

  @Override
  public Rational subtract(Float t2) {
    return subtract(Rational.wrap(t2,1));
  }

  @Override
  public Rational multiply(Float t2) {
    return multiply(Rational.wrap(t2,1));
  }

  @Override
  public Rational divide(Float t2) {
    return divide(Rational.wrap(t2,1));
  }

  @Override
  public Rational add(Double t2) {
    return add(Rational.wrap(t2,1));
  }

  @Override
  public Rational subtract(Double t2) {
    return subtract(Rational.wrap(t2,1));
  }

  @Override
  public Rational multiply(Double t2) {
    return multiply(Rational.wrap(t2,1));
  }

  @Override
  public Rational divide(Double t2) {
    return divide(Rational.wrap(t2,1));
  }

  //-----------------------------------------------------------------------------------------------
  //endregion

  //region Comparison
  //-----------------------------------------------------------------------------------------------

  @Override
  public boolean isInteger() {
    return false;
  }

  //-----------------------------------------------------------------------------------------------
  //endregion

  public Tuple2<NumExt, NumExt> toTuple() {
    return Tuple.of(numWrap, denomWrap);
  }

  public String toString() {
    return numWrap.toString() + "/" + denomWrap.toString();
  }
}
