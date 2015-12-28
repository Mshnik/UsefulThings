package common.math;

import functional.impl.Function1;

//TODO - SPEC
public class RecursiveSequence extends Sequence {

  private Function1<Double, Double> recF;

  public RecursiveSequence(double initial, Function1<Double, Double> func) {
    super(initial);
    recF = func;
  }

  public double func(int index) {
    return recF.apply(compute(index - 1));
  }
}
