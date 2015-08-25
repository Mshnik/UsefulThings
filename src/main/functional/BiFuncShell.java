package functional;

public interface BiFuncShell<A, B> extends FuncShell {

  UnitFuncShell partialApply(A a, B b);

  SingleFuncShell<B> partialApply(A a);

  BiConsumer<A, B> discardReturn();
}
