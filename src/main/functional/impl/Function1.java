package functional.impl;

import functional._1ArgShell;
import functional._NonExShell;
import functional._ReturnShell;
import functional.impl.ex.Function1Ex;

import java.util.Objects;

@FunctionalInterface
public interface Function1<A, R> extends java.util.function.Function<A, R>, _1ArgShell<A>, _ReturnShell<R>, _NonExShell, Function1Ex<A, R> {
  R apply(A a);

  default Function1Ex<A, R> asEx() {
    return this::apply;
  }

  default Supplier<R> partialApply(A a) {
    return () -> apply(a);
  }

  default Supplier<R> lazyApply(Supplier<A> aSupplier) {
    return () -> apply(aSupplier.apply());
  }

  default Consumer1<A> discardReturn() {
    return this::apply;
  }

  default <V> Function1<V, R> compose(Function1<? super V, ? extends A> before) {
    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
  }

  default Function1<A, R> andThen(Unit after) {
    return (a) -> {
      R r = apply(a);
      after.apply();
      return r;
    };
  }

  default Function1<A, R> butFirst(Unit before) {
    return (a) -> {
      before.apply();
      return apply(a);
    };
  }
}

