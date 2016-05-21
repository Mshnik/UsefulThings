package common.math;

import functional.impl.Function1;

import java.util.function.Function;

/**
 * @author Mshnik
 */
public class Real extends NumExt {

  private static final int DEFAULT_SIGFIGS = 6;

  private Function1<Integer, NumExt> func;

  private Real(Function1<Integer, NumExt> func) {
    this.func = func;
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

  @Override
  public boolean isInteger() {
    return false;
  }

  @Override
  public NumExt add(Real t2) {
    throw new UnsupportedOperationException(); //TODO
  }

  @Override
  public NumExt subtract(Real t2) {
    throw new UnsupportedOperationException(); //TODO
  }

  @Override
  public NumExt multiply(Real t2) {
    throw new UnsupportedOperationException(); //TODO
  }

  @Override
  public NumExt divide(Real t2) {
    throw new UnsupportedOperationException(); //TODO
  }

  @Override
  public NumExt negate() {
    return wrap(func.andThen(NumExt::negate));
  }

  @Override
  public NumExt add(Byte t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public NumExt subtract(Byte t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public NumExt multiply(Byte t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public NumExt divide(Byte t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public NumExt add(Short t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public NumExt subtract(Short t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public NumExt multiply(Short t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public NumExt divide(Short t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public NumExt add(Integer t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public NumExt subtract(Integer t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public NumExt multiply(Integer t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public NumExt divide(Integer t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public NumExt add(Long t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public NumExt subtract(Long t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public NumExt multiply(Long t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public NumExt divide(Long t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public NumExt add(Float t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public NumExt subtract(Float t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public NumExt multiply(Float t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public NumExt divide(Float t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public NumExt add(Double t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public NumExt subtract(Double t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public NumExt multiply(Double t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public NumExt divide(Double t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }

  @Override
  public NumExt add(Rational t2) {
    return wrap(func.andThen(x -> x.add(t2)));
  }

  @Override
  public NumExt subtract(Rational t2) {
    return wrap(func.andThen(x -> x.subtract(t2)));
  }

  @Override
  public NumExt multiply(Rational t2) {
    return wrap(func.andThen(x -> x.multiply(t2)));
  }

  @Override
  public NumExt divide(Rational t2) {
    return wrap(func.andThen(x -> x.divide(t2)));
  }
}
