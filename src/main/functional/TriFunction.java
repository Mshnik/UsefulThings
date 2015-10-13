package functional;

import java.util.Objects;

@FunctionalInterface
public interface TriFunction<A, B, C, R> extends TriFuncShell<A, B, C> {
  R apply(A a, B B, C c);

  default BiFunction<B, C, R> partialApply(A a) {
    return (b, c) -> apply(a, b, c);
  }

  default BiFunction<B, C, R> lazyApply(Supplier<A> aSupplier) {
    return (b, c) -> apply(aSupplier.apply(), b, c);
  }

  default Function<C, R> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default Supplier<R> partialApply(A a, B b, C c) {
    return partialApply(a).partialApply(b).partialApply(c);
  }

  default Function<C, R> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Function<C, R> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default Supplier<R> partialLazyApply(Supplier<A> aSupplier, B b, C c) {
    return lazyApply(aSupplier).partialApply(b).partialApply(c);
  }

  default Supplier<R> partialLazyApply(A a, Supplier<B> bSupplier, C c) {
    return partialApply(a).lazyApply(bSupplier).partialApply(c);
  }

  default Supplier<R> partialLazyApply(A a, B b, Supplier<C> cSupplier) {
    return partialApply(a).partialApply(b).lazyApply(cSupplier);
  }

  default Supplier<R> partialLazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, C c) {
    return lazyApply(aSupplier).lazyApply(bSupplier).partialApply(c);
  }

  default Supplier<R> partialLazyApply(A a, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return partialApply(a).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default Supplier<R> partialLazyApply(Supplier<A> aSupplier, B b, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).partialApply(b).lazyApply(cSupplier);
  }

  default Function<C, R> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default Supplier<R> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier, Supplier<C> cSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier).lazyApply(cSupplier);
  }

  default <S> TriFunction<A, B, C, S> andThen(java.util.function.Function<? super R, ? extends S> next) {
    Objects.requireNonNull(next);
    return (a, b, c) -> next.apply(apply(a, b, c));
  }

  default TriFunction<C, A, B, R> rotate() {
    return (c, a, b) -> apply(a, b, c);
  }

  default TriConsumer<A, B, C> discardReturn() {
    return (a, b, c) -> apply(a, b, c);
  }
}