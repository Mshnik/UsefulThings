package common.math;

import functional.impl.Function1;

import java.util.function.Function;

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

  @Override
  public boolean isInteger() {
    return false;
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
  public Real divide(Real t2) {
    throw new UnsupportedOperationException("How to handle div zero?");
  }

  @Override
  public Real negate() {
    return wrap(func.andThen(NumExt::negate));
  }

  @Override
  public Real add(Byte t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public Real subtract(Byte t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public Real multiply(Byte t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public Real divide(Byte t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public Real add(Short t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public Real subtract(Short t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public Real multiply(Short t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public Real divide(Short t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public Real add(Integer t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public Real subtract(Integer t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public Real multiply(Integer t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public Real divide(Integer t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public Real add(Long t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public Real subtract(Long t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public Real multiply(Long t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public Real divide(Long t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public Real add(Float t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public Real subtract(Float t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public Real multiply(Float t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public Real divide(Float t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public Real add(Double t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public Real subtract(Double t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public Real multiply(Double t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public Real divide(Double t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
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
}
