package functional;

@FunctionalInterface
public interface TriConsumer<A,B,C> extends FuncShell {
	void apply(A a, B b, C c);
	
	default Unit partialApply(A a, B b, C c) {
		return () -> apply(a, b, c);
	}
	
	default Consumer<C> partialApply(A a, B b) {
		return (c) -> apply(a,b,c);
	}
	
	default BiConsumer<B, C> partialApply(A a) {
		return (b, c) -> apply(a,b,c);
	}
}