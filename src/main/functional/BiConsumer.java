package functional;

@FunctionalInterface
public interface BiConsumer<A, B> extends java.util.function.BiConsumer<A, B>, BiFuncShell<A, B> {
  void apply(A a, B b);

  default void accept(A a, B b) {
    apply(a, b);
  }

  default Unit partialApply(A a, B b) {
    return () -> apply(a, b);
  }

  default Consumer<B> partialApply(A a) {
    return (b) -> apply(a, b);
  }

  default BiConsumer<B, A> rotate() {
    return (b, a) -> apply(a, b);
  }

  default BiConsumer<A, B> discardReturn() {
    return this;
  }
}
