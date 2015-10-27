package common.math;

import functional.Function;

public class RecursiveSequence extends Sequence {

  private Function<Double, Double> recF;

  public RecursiveSequence(double initial, Function<Double, Double> func) {
    super(initial);
    recF = func;
  }

  public double func(int index) {
    return recF.apply(compute(index - 1));
  }
}
