package functional;

import java.util.Objects;

@FunctionalInterface
public interface TriFunction<A,B,C,R> extends TriFuncShell<A,B,C> {
	R apply(A a, B B, C c);
	
	default Supplier<R> partialApply(A a, B b, C c) {
		return () -> apply(a, b, c);
	}
	
	default Function<C, R> partialApply(A a, B b) {
		return (c) -> apply(a,b,c);
	}
	
	default BiFunction<B, C, R> partialApply(A a) {
		return (b, c) -> apply(a,b,c);
	}
	
	default <S> TriFunction<A, B, C, S> andThen(java.util.function.Function<? super R,? extends S> next) {
		Objects.requireNonNull(next);
		return (a, b, c) -> next.apply(apply(a, b, c));
	}	 
	
	default TriFunction<C,A,B,R> rotate() {
		return (c,a,b) -> apply(a,b,c);
	}
	
	default TriConsumer<A,B,C> discardReturn() {
		return (a,b,c) -> apply(a,b,c);
	}
}