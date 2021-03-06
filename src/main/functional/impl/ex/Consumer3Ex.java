package functional.impl.ex;

import functional._3ArgShell;
import functional._ExShell;
import functional._NonReturnShell;
import functional.impl.*;

import java.util.Objects;

@FunctionalInterface
public interface Consumer3Ex<A, B, C> extends _ExShell, _3ArgShell<A, B, C>, _NonReturnShell {
  void apply(A a, B b, C c) throws Throwable;

  default Consumer3<A, B, C> withHandler(Consumer1<Throwable> handler) {
    return (a, b, c) -> {
      try {
        apply(a, b, c);
      } catch(Throwable t) {
        handler.apply(t);
      }
    };
  }

  default Consumer3<A, B, C> ignoreThrowable() {
    return withHandler(DO_NOTHING);
  }

  default Consumer2Ex<B, C> partialApply(A a) {
    return (b, c) -> apply(a, b, c);
  }

  default Consumer2Ex<B, C> lazyApply(Supplier<A> aSupplier) {
    return (b, c) -> apply(aSupplier.apply(), b, c);
  }

  default Consumer1Ex<C> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default UnitEx partialApply(A a, B b, C c) {
    return partialApply(a).partialApply(b).partialApply(c);
  }

  default Consumer1Ex<C> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Consumer1Ex<C> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default UnitEx partialLazyApply(Supplier<A> aSupplier, B b, C c) {
    return lazyApply(aSupplier).partialApply(b).partialApply(c);
  }

  default UnitEx partialLazyApply(A a, Supplier<B> bSupplier, C c) {
    return partialApply(a).lazyApply(bSupplier).partialApply(c);
  }

  default UnitEx partialLazyApply(A a, B b, Supplier<C> cSupplier) {
    return partialApply(a).partialApply(b).lazyApply(cSupplier);
  }

  default UnitEx partialLazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, C c) {
    return lazyApply(aSupplier).lazyApply(bSupplier).partialApply(c);
  }

  default UnitEx partialLazyApply(A a, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return partialApply(a).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default UnitEx partialLazyApply(Supplier<A> aSupplier, B b, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).partialApply(b).lazyApply(cSupplier);
  }

  default Consumer1Ex<C> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default UnitEx lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default Consumer3Ex<C, A, B> rotate() {
    return (c, a, b) -> apply(a, b, c);
  }

  default Consumer3Ex<A, B, C> discardReturn() {
    return this;
  }

  default Consumer3Ex<A, B, C> compose(Consumer3Ex<? super A, ? super B, ? super C> after) {
    Objects.requireNonNull(after);
    return (a, b, c) -> {
      apply(a, b, c);
      after.apply(a, b, c);
    };
  }

  default Consumer3Ex<A, B, C> andThen(Unit after) {
    return (a, b, c) -> {
      apply(a, b, c);
      after.apply();
    };
  }

  default Consumer3Ex<A, B, C> butFirst(UnitEx before) {
    return (a, b, c) -> {
      before.apply();
      apply(a, b, c);
    };
  }

  default Consumer3Ex<A, B, C> butFirst(Unit before) {
    return (a, b, c) -> {
      before.apply();
      apply(a, b, c);
    };
  }
}