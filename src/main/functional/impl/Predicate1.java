package functional.impl;

import functional._NonExShell;
import functional._ReturnShell;

import java.util.Objects;

@FunctionalInterface
public interface Predicate1<A> extends java.util.function.Predicate<A>, Function1<A, Boolean>, _ReturnShell<Boolean>, _NonExShell {
  Boolean apply(A a);

  default boolean test(A a) {
    return apply(a);
  }

  default Predicate1<A> negate() {
    return (a) -> !apply(a);
  }

  default Predicate1<A> butFirst(Unit before) {
    return (a) -> {
      before.apply();
      return apply(a);
    };
  }

  default Predicate1<A> and(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> apply(a) && other.test(a);
  }

  default Predicate1<A> or(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> apply(a) || other.test(a);
  }

  default Predicate1<A> nand(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> !(apply(a) && other.test(a));
  }

  default Predicate1<A> nor(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> !(apply(a) || other.test(a));
  }

  default Predicate1<A> xor(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> apply(a) ^ other.test(a);
  }

  default Predicate1<A> xnor(java.util.function.Predicate<? super A> other) {
    Objects.requireNonNull(other);
    return (a) -> !(apply(a) ^ other.test(a));
  }

  default Consumer1<A> discardReturn() {
    return (a) -> apply(a);
  }
}
