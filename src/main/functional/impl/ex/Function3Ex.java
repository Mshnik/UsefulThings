package functional.impl.ex;

import functional._3ArgShell;
import functional._ExShell;
import functional._ReturnShell;
import functional.impl.*;

import java.util.Objects;

@FunctionalInterface
public interface Function3Ex<A, B, C, R> extends _ExShell, _3ArgShell<A, B, C>, _ReturnShell<R> {
  R apply(A a, B B, C c) throws Throwable;

  default Function2Ex<B, C, R> partialApply(A a) {
    return (b, c) -> apply(a, b, c);
  }

  default Function2Ex<B, C, R> lazyApply(Supplier<A> aSupplier) {
    return (b, c) -> apply(aSupplier.apply(), b, c);
  }

  default Function1Ex<C, R> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default SupplierEx<R> partialApply(A a, B b, C c) {
    return partialApply(a).partialApply(b).partialApply(c);
  }

  default Function1Ex<C, R> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Function1Ex<C, R> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default SupplierEx<R> partialLazyApply(Supplier<A> aSupplier, B b, C c) {
    return lazyApply(aSupplier).partialApply(b).partialApply(c);
  }

  default SupplierEx<R> partialLazyApply(A a, Supplier<B> bSupplier, C c) {
    return partialApply(a).lazyApply(bSupplier).partialApply(c);
  }

  default SupplierEx<R> partialLazyApply(A a, B b, Supplier<C> cSupplier) {
    return partialApply(a).partialApply(b).lazyApply(cSupplier);
  }

  default SupplierEx<R> partialLazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, C c) {
    return lazyApply(aSupplier).lazyApply(bSupplier).partialApply(c);
  }

  default SupplierEx<R> partialLazyApply(A a, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return partialApply(a).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default SupplierEx<R> partialLazyApply(Supplier<A> aSupplier, B b, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).partialApply(b).lazyApply(cSupplier);
  }

  default Function1Ex<C, R> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default SupplierEx<R> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default <S> Function3Ex<A, B, C, S> andThen(java.util.function.Function<? super R, ? extends S> next) {
    Objects.requireNonNull(next);
    return (a, b, c) -> next.apply(apply(a, b, c));
  }

  default Function3Ex<C, A, B, R> rotate() {
    return (c, a, b) -> apply(a, b, c);
  }

  default Consumer3Ex<A, B, C> discardReturn() {
    return this::apply;
  }

  default Function3Ex<A, B, C, R> butFirst(Unit before) {
    return (a, b, c) -> {
      before.apply();
      return apply(a, b, c);
    };
  }
}