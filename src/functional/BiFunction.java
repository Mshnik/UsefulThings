package functional;

@FunctionalInterface
public interface BiFunction<A,B,R> extends FuncShell {
	R apply(A a, B b);
	
	default Supplier<R> partialApply(A a, B b) {
		return () -> apply(a, b);
	}
	
	default Function<B, R> partialApply(A a) {
		return (b) -> apply(a,b);
	}
	
	default <C> TriFunction<A,B,C,R> unApply(Class<C> clazz) {
		return (a,b,c) -> apply(a,b);
	}
	
	default BiFunction<B,A,R> rotate() {
		return (b,a) -> apply(a,b);
	}
	
	default <S> BiFunction<A, B, S> andThen(Function<R,S> next) {
		return (a, b) -> next.apply(apply(a, b));
	}	 
}
