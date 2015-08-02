package functional;

@FunctionalInterface
public interface BiFunction<A,B,R> extends java.util.function.BiFunction<A,B,R>, BiFuncShell<A, B> {
	R apply(A a, B b);
	
	default Supplier<R> partialApply(A a, B b) {
		return () -> apply(a, b);
	}
	
	default Function<B, R> partialApply(A a) {
		return (b) -> apply(a,b);
	}
	
	default BiFunction<B,A,R> rotate() {
		return (b,a) -> apply(a,b);
	}
	
	default BiConsumer<A,B> discardReturn() {
		return (a, b) -> apply(a, b);
	}
}
