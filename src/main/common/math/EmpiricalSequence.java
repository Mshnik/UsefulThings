package common.math;

import functional.impl.Function1;

//TODO - SPEC
public class EmpiricalSequence extends Sequence {

  private Function1<Integer, Double> f;

  public EmpiricalSequence(Function1<Integer, Double> func) {
    super(func.apply(0));
    f = func;
  }

  @Override
  public double func(int index) {
    return f.apply(index);
  }
}
