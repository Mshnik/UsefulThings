package functional.impl.ex;

import functional._2ArgShell;
import functional._ExShell;
import functional._ReturnShell;
import functional.impl.Supplier;
import functional.impl.Unit;

import java.util.Objects;

@FunctionalInterface
public interface Function2Ex<A, B, R> extends _ExShell, _2ArgShell<A, B>, _ReturnShell<R> {
  R apply(A a, B b) throws Throwable;

  default Function1Ex<B, R> partialApply(A a) {
    return (b) -> apply(a, b);
  }

  default Function1Ex<B, R> lazyApply(Supplier<A> aSupplier) {
    return (b) -> apply(aSupplier.apply(), b);
  }

  default SupplierEx<R> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default SupplierEx<R> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default SupplierEx<R> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default SupplierEx<R> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Function2Ex<B, A, R> rotate() {
    return (b, a) -> apply(a, b);
  }

  default Consumer2Ex<A, B> discardReturn() {
    return this::apply;
  }

  default <V> Function2Ex<A, B, V> andThen(Function1Ex<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (a, b) -> after.apply(apply(a, b));
  }

  default Function2Ex<A, B, R> butFirst(UnitEx before) {
    return (a, b) -> {
      before.apply();
      return apply(a, b);
    };
  }

  default Function2Ex<A, B, R> butFirst(Unit before) {
    return (a, b) -> {
      before.apply();
      return apply(a, b);
    };
  }
}
