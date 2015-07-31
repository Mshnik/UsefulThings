package functional;

@FunctionalInterface
public interface TriFunction<A,B,C,R> extends FuncShell {
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
	
	default <S> TriFunction<A, B, C, S> andThen(Function<R,S> next) {
		return (a, b, c) -> next.apply(apply(a, b, c));
	}	 
	
	default TriFunction<C,A,B,R> rotate() {
		return (c,a,b) -> apply(a,b,c);
	}
}