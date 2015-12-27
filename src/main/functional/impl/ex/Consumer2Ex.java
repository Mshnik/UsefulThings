package functional.impl.ex;

import functional._2ArgShell;
import functional._ExShell;
import functional._NonReturnShell;
import functional.impl.Consumer1;
import functional.impl.Consumer2;
import functional.impl.Supplier;
import functional.impl.Unit;

@FunctionalInterface
public interface Consumer2Ex<A, B> extends _ExShell, _2ArgShell<A, B>, _NonReturnShell {
  void apply(A a, B b) throws Throwable;

  default Consumer2<A, B> withHandler(Consumer1<Throwable> handler) {
    return (a, b) -> {
      try {
        apply(a, b);
      } catch(Throwable t) {
        handler.apply(t);
      }
    };
  }

  default Consumer2<A, B> ignoreThrowable() {
    return withHandler(DO_NOTHING);
  }

  default Consumer1Ex<B> partialApply(A a) {
    return (b) -> apply(a, b);
  }

  default Consumer1Ex<B> lazyApply(Supplier<A> aSupplier) {
    return (b) -> apply(aSupplier.apply(), b);
  }

  default UnitEx partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default UnitEx partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default UnitEx partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default UnitEx lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Consumer2Ex<B, A> rotate() {
    return (b, a) -> apply(a, b);
  }

  default Consumer2Ex<A, B> discardReturn() {
    return this;
  }

  default Consumer2Ex<A, B> butFirst(Unit before) {
    return (a, b) -> {
      before.apply();
      apply(a, b);
    };
  }
}
