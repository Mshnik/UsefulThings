package functional.util;

import functional.impl.Function1;
import functional.impl.Function2;

//TODO - SPEC
//TODO - TEST
public class FunctionState<I,S> {

  private S currentVal;
  private Function1<S, S> stateReportFunction;
  private Function2<I, S, S> addDataFunction;
  private Function2<I, S, S> removeDataFunction;

  public FunctionState(S initialVal, Function2<I, S, S> addDataFunction, Function2<I, S, S> removeDataFunction) {
    this(initialVal, (s) -> s, addDataFunction, removeDataFunction);
  }

  public FunctionState(S initialVal, Function1<S, S> stateReportFunc, Function2<I, S, S> addDataFunction, Function2<I, S, S> removeDataFunction) {
    this.stateReportFunction = stateReportFunc;
    this.currentVal = initialVal;
    this.addDataFunction = addDataFunction;
    this.removeDataFunction = removeDataFunction;
  }

  public S getCurrentVal() {
    return stateReportFunction.apply(currentVal);
  }

  public void addData(I data) {
    currentVal = addDataFunction.apply(data, currentVal);
  }

  public void removeData(I data) {
    currentVal = removeDataFunction.apply(data, currentVal);
  }
}
