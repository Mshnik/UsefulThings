package common.math;

/**
 * @author Mshnik
 */
public class Rational extends NumExt implements Comparable<Number>{

  public static final Rational ZERO = Rational.wrap(0, 1);
  public static final Rational ONE = Rational.wrap(1,1);
  public static final Rational NEG_ONE = Rational.wrap(-1,1);

  private final NumExt numWrap;
  private final NumExt denomWrap;

  private Rational(NumExt numerator, NumExt denominator)  {
    this.numWrap = numerator;
    this.denomWrap = denominator;
  }

  public static Rational wrap(Number numerator, Number denominator) throws IllegalArgumentException{
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
    NumExt gcd = numWrap.gcd(denomWrap);

    return new Rational(numWrap.divide(gcd),denomWrap.divide(gcd));
  }

  public static Rational wrapNum(Number n) {
    return NumExt.applyByNumType(n, x -> wrap(n, 1),x -> wrap(n, 1),x -> wrap(n, 1),x -> wrap(n, 1),
                                    x -> wrap(n, 1),x -> wrap(n, 1),x -> wrap(n, 1),x->x);
  }

  public Rational negate() {
    return Rational.wrap(numWrap.negate(), denomWrap);
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

  public boolean isZero() {
    return numWrap.isZero();
  }

  public int signum() {
    return numWrap.signum();
  }

  public Rational invert() {
    return Rational.wrap(denomWrap, numWrap);
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
  public int compareTo(Number o) {
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

  @Override
  public boolean isInteger() {
    return false;
  }

  public String toString() {
    return numWrap.toString() + "/" + denomWrap.toString();
  }

  @Override
  public Number getVal() {
    return doubleValue();
  }
}
