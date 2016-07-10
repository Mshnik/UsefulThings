package common.math;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * //TODO - SPEC
 * @author Mshnik
 */
public abstract class NumExt extends Number implements Comparable<Number> {

  public static final NumExt ZERO = StdNumExt.ZERO;
  public static final NumExt ONE = StdNumExt.ONE;
  public static final NumExt TWO = StdNumExt.TWO;
  public static final NumExt NEG_ONE = StdNumExt.NEG_ONE;

  //region Creation
  //-----------------------------------------------------------------------------------------------

  public static NumExt wrap(Number t) {
    return applyByNumType(t, x -> x, Rational::wrap, Real::wrap, StdNumExt::wrapStd);
  }

  //-----------------------------------------------------------------------------------------------
  //endregion

  //region Util
  //-----------------------------------------------------------------------------------------------

  public String toString() {
    return getVal().toString();
  }

   static <R> R applyByNumType(Number n,
                                  Function<NumExt, R> numExtFunc,
                                  Function<Rational, R> rationalFunc,
                                  Function<Real, R> realFunc)
      throws UnsupportedOperationException {
    if (n instanceof Rational){
      return rationalFunc.apply((Rational)n);
    } else if (n instanceof Real){
      return realFunc.apply((Real)n);
    } else if (n instanceof NumExt) {
      return numExtFunc.apply((NumExt)n);
    } else {
      throw new UnsupportedOperationException("Unsupported Numerical Type " + n.getClass());
    }
  }

  static <R> R applyByNumType(Number n,
                              Function<NumExt, R> numExtFunc,
                              Function<Rational, R> rationalFunc,
                              Function<Real, R> realFunc,
                              Function<Number, R> defaultFunc) {
    if (n instanceof Rational){
      return rationalFunc.apply((Rational)n);
    } else if (n instanceof Real){
      return realFunc.apply((Real)n);
    } else if (n instanceof NumExt) {
      return numExtFunc.apply((NumExt)n);
    } else {
      return defaultFunc.apply(n);
    }
  }

  static <R> R applyByNumType(Number n,
                              Function<NumExt, R> numExtFunc,
                              Function<Rational, R> rationalFunc,
                              Function<Real, R> realFunc,
                              Function<Byte, R> byteFunc, Function<Short, R> shortFunc,
                              Function<Integer, R> intFunc, Function<Long, R> longFunc,
                              Function<Float, R> floatFunc, Function<Double, R> doubleFunc)
      throws UnsupportedOperationException {
    if (n instanceof NumExt) {
      if (n instanceof Rational) {
        return rationalFunc.apply((Rational) n);
      } else if (n instanceof Real) {
        return realFunc.apply((Real) n);
      } else {
        return numExtFunc.apply((NumExt) n);
      }
    } else {
        if (n instanceof Byte) {
          return byteFunc.apply((Byte)n);
        } else if (n instanceof Short) {
          return shortFunc.apply((Short)n);
        } else if (n instanceof Integer) {
          return intFunc.apply((Integer)n);
        } else if (n instanceof Long) {
          return longFunc.apply((Long)n);
        } else if (n instanceof Float) {
          return floatFunc.apply((Float)n);
        } else if (n instanceof Double) {
          return doubleFunc.apply((Double)n);
        } else {
          throw new UnsupportedOperationException("Unsupported numerical type " + n.getClass());
        }
    }
  }

  //-----------------------------------------------------------------------------------------------
  //endregion

  //region Getters
  //-----------------------------------------------------------------------------------------------

  public abstract Number getVal();

  public abstract boolean isInteger();

  public abstract <X extends Number> X getAs(Class<X> clazz);

  public abstract Stream<Number> toStream();

  public abstract int intValue();

  public abstract long longValue();

  public abstract float floatValue();

  public abstract double doubleValue();

  public abstract double fractionalValue();

  public abstract int signum();

  //-----------------------------------------------------------------------------------------------
  //endregion

  //region Mutators
  //-----------------------------------------------------------------------------------------------

  public abstract NumExt apply(Function<Number,Number> f);

  public abstract NumExt asByte();

  public abstract NumExt asShort();

  public abstract NumExt asInt();

  public abstract NumExt asFloat();

  public abstract NumExt asDouble();

  public final Rational asRational() {
    return Rational.wrap(this);
  }

  public final Real asReal() {
    return Real.wrap(this);
  }

  public abstract NumExt round();

  public abstract NumExt roundUp();

  public abstract NumExt roundDown();

  //endregion

  //region Arithmetic
  //-----------------------------------------------------------------------------------------------
  public abstract NumExt negate();

  public abstract NumExt abs();

  public abstract NumExt mod(Number n);

  public abstract NumExt gcd(Number n);

  public abstract NumExt log(double base);

  //region Arithmetic-Stubs

  public abstract NumExt add(Number n);
  public abstract NumExt subtract(Number n);
  public abstract NumExt multiply(Number n);
  public abstract NumExt divide(Number n);

  public abstract NumExt add(StdNumExt t2);
  public abstract NumExt subtract(StdNumExt t2);
  public abstract NumExt multiply(StdNumExt t2);
  public abstract NumExt divide(StdNumExt t2);

  public abstract NumExt add(Rational t2);
  public abstract NumExt subtract(Rational t2);
  public abstract NumExt multiply(Rational t2);
  public abstract NumExt divide(Rational t2);

  public abstract NumExt add(Real t2);
  public abstract NumExt subtract(Real t2);
  public abstract NumExt multiply(Real t2);
  public abstract NumExt divide(Real t2);

  //endregion

  //-----------------------------------------------------------------------------------------------
  //endregion

  //region Comparison
  //-----------------------------------------------------------------------------------------------

  public final boolean equals(NumExt n1) {
    return this == n1 || n1 != null && Math.abs(n1.doubleValue() - doubleValue()) == 0.0;
  }

  public final boolean equals(Object o) {
    if (this == o) return true;
    if (! (o instanceof NumExt)) return false;

    NumExt n = (NumExt)o;
    return equals(n);
  }

  public final int hashCode() {
    return intValue();
  }

  public final int compareTo(Number n) {
    return subtract(n).signum();
  }

  public final boolean gt(Number n) {
    return compareTo(n) > 0;
  }

  public final boolean gte(Number n) {
    return compareTo(n) >= 0;
  }

  public final boolean lt(Number n) {
    return compareTo(n) < 0;
  }

  public final boolean lte(Number n) {
    return compareTo(n) <= 0;
  }

  public final boolean isZero() {
    return equals(ZERO);
  }

  public final boolean isZero(double tolerance) {
    return abs().compareTo(tolerance) < 0;
  }

  //-----------------------------------------------------------------------------------------------
  //endregion
}
