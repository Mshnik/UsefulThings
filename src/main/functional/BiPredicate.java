package functional;

import java.util.Objects;

public interface BiPredicate<A, B> extends java.util.function.BiPredicate<A, B>, BiFuncShell<A, B> {
  boolean apply(A a, B b);

  default boolean test(A a, B b) {
    return apply(a, b);
  }

  default Supplier<Boolean> partialApply(A a, B b) {
    return () -> apply(a, b);
  }

  default Predicate<B> partialApply(A a) {
    return (b) -> apply(a, b);
  }

  default BiConsumer<A, B> discardReturn() {
    return (a, b) -> apply(a, b);
  }

  default BiPredicate<B, A> rotate() {
    return (b, a) -> apply(a, b);
  }

  default BiPredicate<A, B> negate() {
    return (a, b) -> !apply(a, b);
  }

  default BiPredicate<A, B> and(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> apply(a, b) && other.test(a, b);
  }

  default BiPredicate<A, B> or(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> apply(a, b) || other.test(a, b);
  }

  default BiPredicate<A, B> nand(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> !(apply(a, b) && other.test(a, b));
  }

  default BiPredicate<A, B> nor(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> !(apply(a, b) || other.test(a, b));
  }

  default BiPredicate<A, B> xor(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> apply(a, b) ^ other.test(a, b);
  }

  default BiPredicate<A, B> xnor(java.util.function.BiPredicate<? super A, ? super B> other) {
    Objects.requireNonNull(other);
    return (a, b) -> !(apply(a, b) ^ other.test(a, b));
  }
}
