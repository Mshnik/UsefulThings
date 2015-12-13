package functional.impl;

import functional._2ArgShell;

import java.util.Objects;

public interface Predicate2<A, B> extends java.util.function.BiPredicate<A, B>, _2ArgShell<A, B> {
  boolean apply(A a, B b);

  default boolean test(A a, B b) {
    return apply(a, b);
  }

  default Predicate<B> partialApply(A a) {
    return (b) -> apply(a, b);
  }

  default Predicate<B> lazyApply(Supplier<A> aSupplier) {
    return (b) -> apply(aSupplier.apply(), b);
  }

  default Supplier<Boolean> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default Supplier<Boolean> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default Supplier<Boolean> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Supplier<Boolean> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Consumer2<A, B> discardReturn() {
    return (a, b) -> apply(a, b);
  }

  default Predicate2<B, A> rotate() {
    return (b, a) -> apply(a, b);
  }

  default Predicate2<A, B> butFirst(Unit before) {
    return (a, b) -> {
      before.apply();
      return apply(a, b);
    };
  }

  default Predicate2<A, B> negate() {
    return (a, b) -> !apply(a, b);
  }

  default Predicate2<A, B> and(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> apply(a, b) && other.test(a, b);
  }

  default Predicate2<A, B> or(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> apply(a, b) || other.test(a, b);
  }

  default Predicate2<A, B> nand(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> !(apply(a, b) && other.test(a, b));
  }

  default Predicate2<A, B> nor(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> !(apply(a, b) || other.test(a, b));
  }

  default Predicate2<A, B> xor(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> apply(a, b) ^ other.test(a, b);
  }

  default Predicate2<A, B> xnor(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> !(apply(a, b) ^ other.test(a, b));
  }
}
