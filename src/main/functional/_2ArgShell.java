package functional;

import functional.impl.Consumer2;
import functional.impl.Supplier;

public interface _2ArgShell<A, B> extends __RootShell {

  _1ArgShell<B> partialApply(A a);

  _1ArgShell<B> lazyApply(Supplier<A> aSupplier);

  default _0ArgShell partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default _0ArgShell partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default _0ArgShell partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default _0ArgShell lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  <X extends _2ArgShell<A, B> & _NonReturnShell> X discardReturn();
}
