package functional.impl;

import functional._2ArgShell;
import functional._NonExShell;
import functional._ReturnShell;
import functional.impl.ex.Function2Ex;

import java.util.Objects;

@FunctionalInterface
public interface Function2<A, B, R> extends java.util.function.BiFunction<A, B, R>, _2ArgShell<A, B>, _ReturnShell<R>, _NonExShell, Function2Ex<A, B, R> {
  R apply(A a, B b);

  default Function2Ex<A, B, R> asEx() {
    return this;
  }

  default Function1<B, R> partialApply(A a) {
    return (b) -> apply(a, b);
  }

  default Function1<B, R> lazyApply(Supplier<A> aSupplier) {
    return (b) -> apply(aSupplier.apply(), b);
  }

  default Supplier<R> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default Supplier<R> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default Supplier<R> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Supplier<R> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Function2<B, A, R> rotate() {
    return (b, a) -> apply(a, b);
  }

  default Consumer2<A, B> discardReturn() {
    return this::apply;
  }

  default <V> Function2<A, B, V> compose(Function1<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (a, b) -> after.apply(apply(a, b));
  }

  default Function2<A, B, R> andThen(Unit after) {
    return (a, b) -> {
      R r = apply(a,b);
      after.apply();
      return r;
    };
  }

  default Function2<A, B, R> butFirst(Unit before) {
    return (a, b) -> {
      before.apply();
      return apply(a, b);
    };
  }
}
