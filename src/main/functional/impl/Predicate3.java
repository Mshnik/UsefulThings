package functional.impl;

import functional._NonExShell;
import functional._ReturnShell;
import functional.impl.ex.Function3Ex;

import java.util.Objects;

@FunctionalInterface
public interface Predicate3<A, B, C> extends Function3<A,B,C,Boolean>, _ReturnShell<Boolean>, _NonExShell {
  default Function3Ex<A, B, C, Boolean> asEx() {
    return this::apply;
  }

  default boolean test(A a, B b, C c) {
    return apply(a, b, c);
  }

  default Predicate2<B, C> partialApply(A a) {
    return (b, c) -> apply(a, b, c);
  }

  default Predicate2<B, C> lazyApply(Supplier<A> aSupplier) {
    return (b, c) -> apply(aSupplier.apply(), b, c);
  }

  default Predicate1<C> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default Predicate1<C> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Predicate1<C> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default Predicate1<C> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Consumer3<A, B, C> discardReturn() {
    return (a, b, c) -> apply(a, b, c);
  }

  default Predicate3<C, A, B> rotate() {
    return (c, a, b) -> apply(a, b, c);
  }

  default Predicate3<A, B, C> negate() {
    return (a, b, c) -> !apply(a, b, c);
  }

  default Predicate3<A, B, C> butFirst(Unit before) {
    return (a, b, c) -> {
      before.apply();
      return apply(a, b, c);
    };
  }

  default Predicate3<A, B, C> and(Predicate3<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> apply(a, b, c) && other.apply(a, b, c);
  }

  default Predicate3<A, B, C> or(Predicate3<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> apply(a, b, c) || other.apply(a, b, c);
  }

  default Predicate3<A, B, C> nand(Predicate3<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> !(apply(a, b, c) && other.apply(a, b, c));
  }

  default Predicate3<A, B, C> nor(Predicate3<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> !(apply(a, b, c) || other.apply(a, b, c));
  }

  default Predicate3<A, B, C> xor(Predicate3<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> apply(a, b, c) ^ other.apply(a, b, c);
  }

  default Predicate3<A, B, C> xnor(Predicate3<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> !(apply(a, b, c) ^ other.apply(a, b, c));
  }
}