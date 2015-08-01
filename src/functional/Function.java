package functional;

@FunctionalInterface
public interface Function<A, R> extends SingleFuncShell<A> {
	R apply(A a);
	
	default Supplier<R> partialApply(A a) {
		return () -> apply(a);
	}
	
	default <B> BiFunction<A,B,R> unApply(Class<B> clazz) {
		return (a,b) -> apply(a);
	}
	
	default <S> Function<A, S> andThen(Function<R,S> next) {
		return (a) -> next.apply(apply(a));
	}
	
	default Consumer<A> discardReturn() {
		return (a) -> apply(a);
	}
}
