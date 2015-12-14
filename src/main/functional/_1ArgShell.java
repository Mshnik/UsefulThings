package functional;

import functional.impl.Supplier;

public interface _1ArgShell<A> extends __RootShell {

  _0ArgShell partialApply(A a);

  _0ArgShell lazyApply(Supplier<A> aSupplier);

  _1ArgShell<A> discardReturn();
}
