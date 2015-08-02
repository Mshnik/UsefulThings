package functional;

import java.util.Objects;

@FunctionalInterface
public interface Function<A, R> extends java.util.function.Function<A,R>, SingleFuncShell<A> {
	R apply(A a);
	
	default Supplier<R> partialApply(A a) {
		return () -> apply(a);
	}
	
	default Consumer<A> discardReturn() {
		return (a) -> apply(a);
	}
	
	default <V> Function<V, R> compose(java.util.function.Function<? super V, ? extends A> before) {
    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
	}
	
  default <V> Function<A, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (a) -> after.apply(apply(a));
  }
}
