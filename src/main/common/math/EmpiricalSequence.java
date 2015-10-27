package common.math;

import functional.Function;

public class EmpiricalSequence extends Sequence {

  private Function<Integer, Double> f;

  public EmpiricalSequence(double initial, Function<Integer, Double> func) {
    super(initial);
    f = func;
  }

  @Override
  public double func(int index) {
    return f.apply(index);
  }
}
