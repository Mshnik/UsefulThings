package common.math;

import common.dataStructures.DeArrList;
import functional.Function;

import java.util.List;

public abstract class Sequence {

  public final Double initial;

  protected DeArrList<Double> computed;

  Sequence(Double initial) {
    this.initial = initial;
    computed = new DeArrList<>();
    computed.add(initial);
  }

  public abstract double func(int index);

  public Double compute(int index) throws IllegalArgumentException {
    if(index < 0) {
      throw new IllegalArgumentException("Can't find sequence term of a negative index " + index);
    }
    if (index < computed.size() && computed.get(index) != null) {
      return computed.get(index);
    }

    Double val = func(index);
    int i = index - computed.size();
    while (i > 0) {
      computed.add(null);
      i--;
    }

    if (index == computed.size()) {
      computed.add(val);
    } else {
      computed.set(index, val);
    }
    return val;
  }

  public List<Double> getComputed() {
    return new DeArrList<>(computed);
  }

  public Sequence add(final Sequence seq) {
    return new Sequence(compute(0) + seq.compute(0)){
      public double func(int index) {
        return Sequence.this.func(index) + seq.func(index);
      }
    };
  }

}
