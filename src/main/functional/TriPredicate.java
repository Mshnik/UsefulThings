package functional;

import java.util.Objects;

@FunctionalInterface
public interface TriPredicate<A, B, C> extends TriFuncShell<A, B, C> {
  boolean apply(A a, B b, C c);

  default boolean test(A a, B b, C c) {
    return apply(a, b, c);
  }

  default BiPredicate<B, C> partialApply(A a) {
    return (b, c) -> apply(a, b, c);
  }

  default BiPredicate<B, C> lazyApply(Supplier<A> aSupplier) {
    return (b, c) -> apply(aSupplier.apply(), b, c);
  }

  default Predicate<C> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default Supplier<Boolean> partialApply(A a, B b, C c) {
    return partialApply(a).partialApply(b).partialApply(c);
  }

  default Predicate<C> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Predicate<C> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default Supplier<Boolean> partialLazyApply(Supplier<A> aSupplier, B b, C c) {
    return lazyApply(aSupplier).partialApply(b).partialApply(c);
  }

  default Supplier<Boolean> partialLazyApply(A a, Supplier<B> bSupplier, C c) {
    return partialApply(a).lazyApply(bSupplier).partialApply(c);
  }

  default Supplier<Boolean> partialLazyApply(A a, B b, Supplier<C> cSupplier) {
    return partialApply(a).partialApply(b).lazyApply(cSupplier);
  }

  default Supplier<Boolean> partialLazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, C c) {
    return lazyApply(aSupplier).lazyApply(bSupplier).partialApply(c);
  }

  default Supplier<Boolean> partialLazyApply(A a, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return partialApply(a).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default Supplier<Boolean> partialLazyApply(Supplier<A> aSupplier, B b, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).partialApply(b).lazyApply(cSupplier);
  }

  default Predicate<C> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Supplier<Boolean> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier).lazyApply(cSupplier);
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