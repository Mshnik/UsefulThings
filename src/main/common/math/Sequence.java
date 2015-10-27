package common.math;

import common.dataStructures.DeArrList;
import functional.Function;

public abstract class Sequence {

  public final Double initial;

  protected DeArrList<Double> computed;

  Sequence(Double initial) {
    this.initial = initial;
    computed = new DeArrList<>();
    computed.add(initial);
  }

  public abstract double func(int index);

  public Double compute(int index) {
    if (index < computed.size() && computed.get(index) != null) {
      return computed.get(index);
    }

    computed.ensureCapacity(index + 1);
    Double val = func(index);
    computed.set(index, val);
    return val;
  }

  public Sequence add(final Sequence seq) {
    return new Sequence(compute(0) + seq.compute(0)){
      public double func(int index) {
        return Sequence.this.func(index) + seq.func(index);
      }
    };
  }

}
