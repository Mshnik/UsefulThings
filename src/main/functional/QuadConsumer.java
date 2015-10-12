package functional;

import java.util.Objects;

@FunctionalInterface
public interface QuadConsumer<A, B, C, D> extends QuadFuncShell<A, B, C, D> {
  void apply(A a, B b, C c, D d);

  default void accept(A a, B b, C c, D d) {
    apply(a, b, c, d);
  }

  default Unit partialApply(A a, B b, C c, D d) {
    return () -> apply(a, b, c, d);
  }

  default Consumer<D> partialApply(A a, B b, C c) {
    return (d) -> apply(a, b, c, d);
  }

  default BiConsumer<C, D> partialApply(A a, B b) {
    return (c, d) -> apply(a, b, c, d);
  }

  default TriConsumer<B, C, D> partialApply(A a) {
    return (b, c, d) -> apply(a, b, c, d);
  }

  default QuadConsumer<D, A, B, C> rotate() {
    return (d, a, b, c) -> apply(a, b, c, d);
  }

  default QuadConsumer<A, B, C, D> discardReturn() {
    return this;
  }

  default QuadConsumer<A, B, C, D> andThen(QuadConsumer<? super A, ? super B, ? super C, ? super D> after) {
    Objects.requireNonNull(after);
    return (a, b, c, d) -> {
      apply(a, b, c, d);
      after.apply(a, b, c, d);
    };
  }
}