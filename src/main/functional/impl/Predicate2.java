package functional.impl;

import functional._NonExShell;
import functional._ReturnShell;
import functional.impl.ex.Function2Ex;

import java.util.Objects;

public interface Predicate2<A, B> extends java.util.function.BiPredicate<A, B>, Function2<A, B, Boolean>, _ReturnShell<Boolean>, _NonExShell {
  Boolean apply(A a, B b);

  default Function2Ex<A, B, Boolean> asEx() {
    return this::apply;
  }

  default boolean test(A a, B b) {
    return apply(a, b);
  }

  default Predicate1<B> partialApply(A a) {
    return (b) -> apply(a, b);
  }

  default Predicate1<B> lazyApply(Supplier<A> aSupplier) {
    return (b) -> apply(aSupplier.apply(), b);
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
