package functional;

import java.util.Objects;

@FunctionalInterface
public interface Predicate<A> extends java.util.function.Predicate<A>, SingleFuncShell<A> {
  boolean apply(A a);

  default boolean test(A a) {
    return apply(a);
  }

  default Supplier<Boolean> partialApply(A a) {
    return () -> apply(a);
  }

  default Consumer<A> discardReturn() {
    return (a) -> apply(a);
  }

  default Predicate<A> negate() {
    return (a) -> !apply(a);
  }

  default Predicate<A> and(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> apply(a) && other.test(a);
  }

  default Predicate<A> or(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> apply(a) || other.test(a);
  }

  default Predicate<A> nand(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> !(apply(a) && other.test(a));
  }

  default Predicate<A> nor(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> !(apply(a) || other.test(a));
  }

  default Predicate<A> xor(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> apply(a) ^ other.test(a);
  }

  default Predicate<A> xnor(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> !(apply(a) ^ other.test(a));
  }
}
