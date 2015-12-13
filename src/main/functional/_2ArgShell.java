package functional;

public interface _2ArgShell<A, B> extends FuncShell {

  _1ArgShell<B> partialApply(A a);

  _1ArgShell<B> lazyApply(Supplier<A> aSupplier);

  default UnitFuncShell partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default UnitFuncShell partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default UnitFuncShell partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default UnitFuncShell lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  BiConsumer<A, B> discardReturn();
}
