package functional;

public interface _1ArgShell<A> extends FuncShell {

  UnitFuncShell partialApply(A a);

  UnitFuncShell lazyApply(Supplier<A> aSupplier);

  Consumer<A> discardReturn();
}
