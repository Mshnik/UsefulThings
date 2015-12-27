package functional.impl;

import functional._1ArgShell;
import functional._NonExShell;
import functional._ReturnShell;
import functional.impl.ex.Function1Ex;

import java.util.Objects;

@FunctionalInterface
public interface Function1<A, R> extends java.util.function.Function<A, R>, _1ArgShell<A>, _ReturnShell<R>, _NonExShell {
  R apply(A a);

  default Supplier<R> partialApply(A a) {
    return () -> apply(a);
  }

  default Supplier<R> lazyApply(Supplier<A> aSupplier) {
    return () -> apply(aSupplier.apply());
  }

  default Consumer1<A> discardReturn() {
    return (a) -> apply(a);
  }

  default <V> Function1<V, R> compose(java.util.function.Function<? super V, ? extends A> before) {
    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
  }

  default <V> Function1<A, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (a) -> after.apply(apply(a));
  }

  default Function1<A, R> butFirst(Unit before) {
    return (a) -> {
      before.apply();
      return apply(a);
    };
  }
}

