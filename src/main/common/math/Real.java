package common.math;

import functional.impl.Function1;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 *
 * Each real is represented as a function, which given an int returns an approximation
 * which is no worse than the true value by the relation:
 *
 *      Max error(i) -> 10^-i
 *
 * In order for this to work, real functions must be defined at all integers (though
 * they clearly diverge quickly over the negative integers).
 *
 * @author Mshnik
 */
public class Real extends NumExt {

  private static final int DEFAULT_SIGFIGS = 8;

  public static Real ZERO = new Real((i) -> NumExt.ZERO);
  public static Real ONE = new Real((i) -> NumExt.ONE);
  public static Real NEG_ONE = new Real((i) -> NumExt.NEG_ONE);

  private final Function1<Integer, NumExt> func;

  /** The approximation of this Real at 0 sigfigs (+- 1 from true value) */
  private final NumExt approx0;

  private Real(Function1<Integer, NumExt> func) {
    this.func = func;
    approx0 = func.apply(0);
  }

  public static Real wrap(Function<Integer, Number> func) {
    return new Real(func.andThen(NumExt::wrap)::apply);
  }

  public static Real wrap(Number n) {
    if (n instanceof Real) {
      return (Real)n;
    }
    return wrap(i -> n);
  }

  @Override
  public Number getVal() {
    return func.apply(DEFAULT_SIGFIGS).getVal();
  }

  public NumExt getVal(int sigfigs) {
    return func.apply(sigfigs);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < DEFAULT_SIGFIGS; i++) {
      sb.append(getVal(i).toString());
      if (i+1 < DEFAULT_SIGFIGS) sb.append(", ");
    }
    return sb.toString();
  }

  public Stream<Number> toStream() {
    return IntStream.iterate(0, x -> x+1).mapToObj(this::getVal);
  }

  @Override
  public int intValue() {
    return 0;
  }

  @Override
  public long longValue() {
    return 0;
  }

  @Override
  public float floatValue() {
    return 0;
  }

  @Override
  public double doubleValue() {
    return 0;
  }

  @Override
  public double fractionalValue() {
    return 0;
  }

  @Override
  public int signum() {
    return 0;
  }

  @Override
  public NumExt apply(Function<Number, Number> f) {
    return null;
  }

  @Override
  public NumExt asByte() {
    return null;
  }

  @Override
  public NumExt asShort() {
    return null;
  }

  @Override
  public NumExt asInt() {
    return null;
  }

  @Override
  public NumExt asFloat() {
    return null;
  }

  @Override
  public NumExt asDouble() {
    return null;
  }

  @Override
  public NumExt round() {
    return null;
  }

  @Override
  public NumExt roundUp() {
    return null;
  }

  @Override
  public NumExt roundDown() {
    return null;
  }

  @Override
  public boolean isInteger() {
    return false;
  }

  @Override
  public <X extends Number> X getAs(Class<X> clazz) {
    return null;
  }

  //region Arithmetic

  @Override
  public Real negate() {
    return wrap(func.andThen(NumExt::negate));
  }

  public Real abs() {
    return wrap(func.andThen(NumExt::abs));
  }

  @Override
  public NumExt mod(Number n) {
    return null;
  }

  @Override
  public NumExt gcd(Number n) {
    return null;
  }

  @Override
  public NumExt log(double base) {
    return null;
  }

  @Override
  public NumExt add(Number n) {
    return null;
  }

  @Override
  public NumExt subtract(Number n) {
    return null;
  }

  @Override
  public NumExt multiply(Number n) {
    return null;
  }

  @Override
  public NumExt divide(Number n) {
    return null;
  }

  @Override
  public Real add(Real t2) {
    //Max Error of returned function:
    //  Err 1 + Err 2
    // 10^-(i+1) * 2 = 10^-1 * 0.1 * 2 = 0.2*10^-i < 10^-1
    return wrap(i -> getVal(i+1).add(t2.getVal(i+1)));
  }

  @Override
  public Real subtract(Real t2) {
    //Max Error of returned function:
    // Err 1 + Err 2
    // 10^-(i+1) * 2 = 10^-1 * 0.1 * 2 = 0.2*10^-i < 10^-1
    return wrap(i -> getVal(i+1).subtract(t2.getVal(i+1)));
  }

  @Override
  public Real multiply(Real t2) {
    //Max Error of returned function:
    // Err 1 * Val 2 + Err 2 + Val 1
    int figs1 = approx0.abs().add(NumExt.ONE).log(10).roundUp().intValue();
    int figs2 = t2.approx0.abs().add(NumExt.ONE).log(10).roundUp().intValue();

    // 10^(-(i+1+figs2)) * val2 + 10^(-(i+1+figs1)) * val1 < 10^-(i+1) * 2 = 10^-1 * 0.1 * 2 = 0.2*10^-i < 10^-1
    return wrap(i -> getVal(i+1+figs2).multiply(t2.getVal(i+1+figs1)));
  }

  @Override

  //TODO - not amazing.. needs some fixup because of div zero.
  //Might not be fixable though
  public Real divide(Real t2) {
    if (t2.equals(ZERO)) {
      throw new ArithmeticException("Divide by Zero");
    }

    //Max Error of returned function:
    // Err 1 * Val 2 + Err 2 + Val 1
    int figs1 = approx0.abs().add(NumExt.ONE).log(10).roundUp().intValue();
    int invFigs2 = t2.getVal(DEFAULT_SIGFIGS).abs().log(10).roundDown().intValue();

    // 10^(-(i+1+invFigs2)) * val2 + 10^(-(i+1+figs1)) * val1 < 10^-(i+1) * 2 = 10^-1 * 0.1 * 2 = 0.2*10^-i < 10^-1
    return wrap(i -> getVal(i+1+invFigs2).multiply(t2.getVal(i+1+figs1)));
  }

  @Override
  public NumExt add(StdNumExt t2) {
    return null;
  }

  @Override
  public NumExt subtract(StdNumExt t2) {
    return null;
  }

  @Override
  public NumExt multiply(StdNumExt t2) {
    return null;
  }

  @Override
  public NumExt divide(StdNumExt t2) {
    return null;
  }

  @Override
  public Real add(Rational t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public Real subtract(Rational t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public Real multiply(Rational t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public Real divide(Rational t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  //endregion
}
