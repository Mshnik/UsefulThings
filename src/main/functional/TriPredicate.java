package functional;

import java.util.Objects;

@FunctionalInterface
public interface TriPredicate<A, B, C> extends TriFuncShell<A, B, C> {
  boolean apply(A a, B b, C c);

  default boolean test(A a, B b, C c) {
    return apply(a, b, c);
  }

  default Supplier<Boolean> partialApply(A a, B b, C c) {
    return () -> apply(a, b, c);
  }

  default Predicate<C> partialApply(A a, B b) {
    return (c) -> apply(a, b, c);
  }

  default BiPredicate<B, C> partialApply(A a) {
    return (b, c) -> apply(a, b, c);
  }

  default TriConsumer<A, B, C> discardReturn() {
    return (a, b, c) -> apply(a, b, c);
  }

  default TriPredicate<C, A, B> rotate() {
    return (c, a, b) -> apply(a, b, c);
  }

  default TriPredicate<A, B, C> negate() {
    return (a, b, c) -> !apply(a, b, c);
  }

  default TriPredicate<A, B, C> and(TriPredicate<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> apply(a, b, c) && other.apply(a, b, c);
  }

  default TriPredicate<A, B, C> or(TriPredicate<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> apply(a, b, c) || other.apply(a, b, c);
  }

  default TriPredicate<A, B, C> nand(TriPredicate<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> !(apply(a, b, c) && other.apply(a, b, c));
  }

  default TriPredicate<A, B, C> nor(TriPredicate<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> !(apply(a, b, c) || other.apply(a, b, c));
  }

  default TriPredicate<A, B, C> xor(TriPredicate<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> apply(a, b, c) ^ other.apply(a, b, c);
  }

  default TriPredicate<A, B, C> xnor(TriPredicate<A, B, C> other) {
    Objects.requireNonNull(other);
    return (a, b, c) -> !(apply(a, b, c) ^ other.apply(a, b, c));
  }
}