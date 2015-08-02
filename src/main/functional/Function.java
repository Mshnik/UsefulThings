package functional;

@FunctionalInterface
public interface Function<A, R> extends java.util.function.Function<A,R>, SingleFuncShell<A> {
	R apply(A a);
	
	default Supplier<R> partialApply(A a) {
		return () -> apply(a);
	}
	
	default Consumer<A> discardReturn() {
		return (a) -> apply(a);
	}
}
