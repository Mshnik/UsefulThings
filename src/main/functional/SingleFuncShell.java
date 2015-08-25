package functional;

public interface SingleFuncShell<A> extends FuncShell {

  UnitFuncShell partialApply(A a);

  Consumer<A> discardReturn();
}
