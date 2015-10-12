package functional;

import java.util.Objects;

@FunctionalInterface
public interface QuadFunction<A, B, C, D, R> extends QuadFuncShell<A, B, C, D> {
  R apply(A a, B B, C c, D d);

  default Supplier<R> partialApply(A a, B b, C c, D d) {
    return () -> apply(a, b, c, d);
  }

  default Function<D, R> partialApply(A a, B b, C c) {
    return (d) -> apply(a, b, c, d);
  }

  default BiFunction<C, D, R> partialApply(A a, B b) {
    return (c, d) -> apply(a, b, c, d);
  }

  default TriFunction<B, C, D, R> partialApply(A a) {
    return (b, c, d) -> apply(a, b, c, d);
  }

  default <S> QuadFunction<A, B, C, D, S> andThen(java.util.function.Function<? super R, ? extends S> next) {
    Objects.requireNonNull(next);
    return (a, b, c, d) -> next.apply(apply(a, b, c, d));
  }

  default QuadFunction<D, A, B, C, R> rotate() {
    return (d, a, b, c) -> apply(a, b, c, d);
  }

  default QuadConsumer<A, B, C, D> discardReturn() {
    return (a, b, c, d) -> apply(a, b, c, d);
  }
}
