package functional;

import java.util.Objects;

@FunctionalInterface
public interface QuadPredicate<A, B, C, D> extends QuadFuncShell<A, B, C, D> {

  boolean apply(A a, B b, C c, D d);

  default boolean test(A a, B b, C c, D d) {
    return apply(a, b, c, d);
  }

  default Supplier<Boolean> partialApply(A a, B b, C c, D d) {
    return () -> apply(a, b, c, d);
  }

  default Predicate<D> partialApply(A a, B b, C c) {
    return (d) -> apply(a, b, c, d);
  }

  default BiPredicate<C, D> partialApply(A a, B b) {
    return (c, d) -> apply(a, b, c, d);
  }

  default TriPredicate<B, C, D> partialApply(A a) {
    return (b, c, d) -> apply(a, b, c, d);
  }

  default QuadConsumer<A, B, C, D> discardReturn() {
    return (a, b, c, d) -> apply(a, b, c, d);
  }

  default QuadPredicate<D, A, B, C> rotate() {
    return (d, a, b, c) -> apply(a, b, c, d);
  }

  default QuadPredicate<A, B, C, D> negate() {
    return (a, b, c, d) -> !apply(a, b, c, d);
  }

  default QuadPredicate<A, B, C, D> and(QuadPredicate<A, B, C, D> other) {
    Objects.requireNonNull(other);
    return (a, b, c, d) -> apply(a, b, c, d) && other.apply(a, b, c, d);
  }

  default QuadPredicate<A, B, C, D> or(QuadPredicate<A, B, C, D> other) {
    Objects.requireNonNull(other);
    return (a, b, c, d) -> apply(a, b, c, d) || other.apply(a, b, c, d);
  }

  default QuadPredicate<A, B, C, D> nand(QuadPredicate<A, B, C, D> other) {
    Objects.requireNonNull(other);
    return (a, b, c, d) -> !(apply(a, b, c, d) && other.apply(a, b, c, d));
  }

  default QuadPredicate<A, B, C, D> nor(QuadPredicate<A, B, C, D> other) {
    Objects.requireNonNull(other);
    return (a, b, c, d) -> !(apply(a, b, c, d) || other.apply(a, b, c, d));
  }

  default QuadPredicate<A, B, C, D> xor(QuadPredicate<A, B, C, D> other) {
    Objects.requireNonNull(other);
    return (a, b, c, d) -> apply(a, b, c, d) ^ other.apply(a, b, c, d);
  }

  default QuadPredicate<A, B, C, D> xnor(QuadPredicate<A, B, C, D> other) {
    Objects.requireNonNull(other);
    return (a, b, c, d) -> !(apply(a, b, c, d) ^ other.apply(a, b, c, d));
  }
}
