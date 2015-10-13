package functional;

public interface SingleFuncShell<A> extends FuncShell {

  UnitFuncShell partialApply(A a);

  UnitFuncShell lazyApply(Supplier<A> aSupplier);

  Consumer<A> discardReturn();
}
