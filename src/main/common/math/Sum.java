package common.math;

import common.dataStructures.DeArrList;
import functional.Function;

public class Sum extends Sequence {

  private Function<Integer, Double> sFunc;

  public Sum(Function<Integer, Double> f) {
    super(0.0);
    sFunc = f;
    computed = new DeArrList<>();
    computed.add(f.apply(0));
  }

  @Override
  public double func(int index) {
    return sFunc.apply(index);
  }

  public Double compute(int index) throws IllegalArgumentException {
    if(index < 0) {
      throw new IllegalArgumentException("Can't find sequence term of a negative index " + index);
    }
    
    if (index < computed.size() && computed.get(index) != null) {
      return computed.get(index);
    }

    Double val = func(index) + compute(index - 1);
    if (index == computed.size()) {
      computed.add(val);
    } else {
      computed.set(index, val);
    }
    return val;

  }
}
