package functional;

import java.util.Objects;

@FunctionalInterface
public interface BiFunction<A, B, R> extends java.util.function.BiFunction<A, B, R>, BiFuncShell<A, B> {
  R apply(A a, B b);

  default Function<B, R> partialApply(A a) {
    return (b) -> apply(a, b);
  }

  default Function<B, R> lazyApply(Supplier<A> aSupplier) {
    return (b) -> apply(aSupplier.apply(), b);
  }

  default Supplier<R> partialApply(A a, B b) {
    return partialApply(a).partialApply(b);
  }

  default Supplier<R> partialLazyApply(A a, Supplier<B> bSupplier) {
    return partialApply(a).lazyApply(bSupplier);
  }

  default Supplier<R> partialLazyApply(Supplier<A> aSupplier, B b) {
    return lazyApply(aSupplier).partialApply(b);
  }

  default Supplier<R> lazyApply(Supplier<A> aSupplier, Supplier<B> bSupplier) {
    return lazyApply(aSupplier).lazyApply(bSupplier);
  }

  default BiFunction<B, A, R> rotate() {
    return (b, a) -> apply(a, b);
  }

  default BiConsumer<A, B> discardReturn() {
    return (a, b) -> apply(a, b);
  }

  default <V> BiFunction<A, B, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (a, b) -> after.apply(apply(a, b));
  }

  default BiFunction<A, B, R> butFirst(Unit before) {
    return (a, b) -> {
      before.apply();
      return apply(a, b);
    };
  }
}
