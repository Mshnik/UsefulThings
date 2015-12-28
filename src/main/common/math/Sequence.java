package common.math;

import common.dataStructures.DeArrList;
import functional.impl.Function2;

import java.util.List;

//TODO - SPEC
public abstract class Sequence {

  public final Double initial;

  protected DeArrList<Double> computed;

  Sequence(Double initial) {
    this.initial = initial;
    computed = new DeArrList<>();
    computed.add(initial);
  }

  public abstract double func(int index);

  public double compute(int index) throws IllegalArgumentException {
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

  public double[] compute(int startIndex, int endIndex) {
    double[] arr = new double[endIndex - startIndex];
    for(int i = startIndex; i < endIndex; i++) {
      arr[i - startIndex] = compute(i);
    }
    return arr;
  }

  public List<Double> getComputed() {
    return new DeArrList<>(computed);
  }

  public Sequence combine(final Sequence seq, Function2<Double, Double, Double> f) {
    return new Sequence(f.apply(compute(0), seq.compute(0))){
      public double func(int index) {
        return f.apply(Sequence.this.func(index),seq.func(index));
      }
    };
  }

  public Sequence negate(){
    return new Sequence(-compute(0)) {
      public double func(int index) {
        return -Sequence.this.func(index);
      }
    };
  }



}
