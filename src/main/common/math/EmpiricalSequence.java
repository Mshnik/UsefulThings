package common.math;

import functional.Function;

public class EmpiricalSequence extends Sequence {

  private Function<Integer, Double> f;

  public EmpiricalSequence(Function<Integer, Double> func) {
    super(func.apply(0));
    f = func;
  }

  @Override
  public double func(int index) {
    return f.apply(index);
  }
}
