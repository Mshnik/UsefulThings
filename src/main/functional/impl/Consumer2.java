package functional.impl;

import functional._2ArgShell;

@FunctionalInterface
public interface Consumer2<A, B> extends java.util.function.BiConsumer<A, B>, _2ArgShell<A, B> {
  void apply(A a, B b);

  default void accept(A a, B b) {
    apply(a, b);
  }

  default Consumer<B> partialApply(A a) {
    return (b) -> apply(a, b);
  }

  default Consumer<B> lazyApply(Supplier<A> aSupplier) {
    return (b) -> apply(aSupplier.apply(), b);
  }

  default Unit partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default Unit partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default Unit partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Unit lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Consumer2<B, A> rotate() {
    return (b, a) -> apply(a, b);
  }

  default Consumer2<A, B> discardReturn() {
    return this;
  }

  default Consumer2<A, B> butFirst(Unit before) {
    return (a, b) -> {
      before.apply();
      apply(a, b);
    };
  }
}
