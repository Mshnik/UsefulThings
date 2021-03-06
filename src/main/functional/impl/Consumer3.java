package functional.impl;

import common.types.Tuple3;
import functional._3ArgShell;
import functional._NonExShell;
import functional._NonReturnShell;
import functional.impl.ex.Consumer3Ex;

import java.util.Objects;

@FunctionalInterface
public interface Consumer3<A, B, C> extends _3ArgShell<A, B, C>, _NonReturnShell, _NonExShell, Consumer3Ex<A, B, C> {
  void apply(A a, B b, C c);

  default void apply(Tuple3<A,B,C> t) {
    apply(t._1, t._2, t._3);
  }

  default Consumer3Ex<A, B, C> asEx() {
    return this::apply;
  }

  default void accept(A a, B b, C c) {
    apply(a, b, c);
  }

  default Consumer2<B, C> partialApply(A a) {
    return (b, c) -> apply(a, b, c);
  }

  default Consumer2<B, C> lazyApply(Supplier<A> aSupplier) {
    return (b, c) -> apply(aSupplier.apply(), b, c);
  }

  default Consumer1<C> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default Unit partialApply(A a, B b, C c) {
    return partialApply(a).partialApply(b).partialApply(c);
  }

  default Consumer1<C> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Consumer1<C> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default Unit partialLazyApply(Supplier<A> aSupplier, B b, C c) {
    return lazyApply(aSupplier).partialApply(b).partialApply(c);
  }

  default Unit partialLazyApply(A a, Supplier<B> bSupplier, C c) {
    return partialApply(a).lazyApply(bSupplier).partialApply(c);
  }

  default Unit partialLazyApply(A a, B b, Supplier<C> cSupplier) {
    return partialApply(a).partialApply(b).lazyApply(cSupplier);
  }

  default Unit partialLazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, C c) {
    return lazyApply(aSupplier).lazyApply(bSupplier).partialApply(c);
  }

  default Unit partialLazyApply(A a, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return partialApply(a).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default Unit partialLazyApply(Supplier<A> aSupplier, B b, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).partialApply(b).lazyApply(cSupplier);
  }

  default Consumer1<C> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Unit lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default Consumer3<C, A, B> rotate() {
    return (c, a, b) -> apply(a, b, c);
  }

  default Consumer3<A, B, C> discardReturn() {
    return this;
  }

  default Consumer3<A, B, C> andThen(Unit after) {
    Objects.requireNonNull(after);
    return (a, b, c) -> {
      apply(a, b, c);
      after.apply();
    };
  }

  default Consumer3<A, B, C> butFirst(Unit before) {
    return (a, b, c) -> {
      before.apply();
      apply(a, b, c);
    };
  }
}