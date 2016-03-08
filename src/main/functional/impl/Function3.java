package functional.impl;

import common.types.Tuple3;
import functional._3ArgShell;
import functional._NonExShell;
import functional._ReturnShell;
import functional.impl.ex.Function3Ex;

import java.util.Objects;

@FunctionalInterface
public interface Function3<A, B, C, R> extends _3ArgShell<A, B, C>, _ReturnShell<R>, _NonExShell, Function3Ex<A, B, C, R> {
  R apply(A a, B B, C c);

  default R apply(Tuple3<A,B,C> t) {
    return apply(t._1, t._2, t._3);
  }

  default Function3Ex<A, B, C, R> asEx() {
    return this;
  }

  default Function2<B, C, R> partialApply(A a) {
    return (b, c) -> apply(a, b, c);
  }

  default Function2<B, C, R> lazyApply(Supplier<A> aSupplier) {
    return (b, c) -> apply(aSupplier.apply(), b, c);
  }

  default Function1<C, R> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default Supplier<R> partialApply(A a, B b, C c) {
    return partialApply(a).partialApply(b).partialApply(c);
  }

  default Function1<C, R> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Function1<C, R> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default Supplier<R> partialLazyApply(Supplier<A> aSupplier, B b, C c) {
    return lazyApply(aSupplier).partialApply(b).partialApply(c);
  }

  default Supplier<R> partialLazyApply(A a, Supplier<B> bSupplier, C c) {
    return partialApply(a).lazyApply(bSupplier).partialApply(c);
  }

  default Supplier<R> partialLazyApply(A a, B b, Supplier<C> cSupplier) {
    return partialApply(a).partialApply(b).lazyApply(cSupplier);
  }

  default Supplier<R> partialLazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, C c) {
    return lazyApply(aSupplier).lazyApply(bSupplier).partialApply(c);
  }

  default Supplier<R> partialLazyApply(A a, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return partialApply(a).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default Supplier<R> partialLazyApply(Supplier<A> aSupplier, B b, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).partialApply(b).lazyApply(cSupplier);
  }

  default Function1<C, R> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Supplier<R> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default <S> Function3<A, B, C, S> compose(Function1<? super R, ? extends S> next) {
    Objects.requireNonNull(next);
    return (a, b, c) -> next.apply(apply(a, b, c));
  }

  default Function3<C, A, B, R> rotate() {
    return (c, a, b) -> apply(a, b, c);
  }

  default Consumer3<A, B, C> discardReturn() {
    return this::apply;
  }

  default Function3<A, B, C, R> andThen(Unit after) {
    return (a, b, c) -> {
      R r = apply(a,b,c);
      after.apply();
      return r;
    };
  }

  default Function3<A, B, C, R> butFirst(Unit before) {
    return (a, b, c) -> {
      before.apply();
      return apply(a, b, c);
    };
  }
}