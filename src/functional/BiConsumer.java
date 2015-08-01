package functional;

@FunctionalInterface
public interface BiConsumer<A,B> extends BiFuncShell<A, B> {
	void apply(A a, B b);
	
	default Unit partialApply(A a, B b) {
		return () -> apply(a, b);
	}
	
	default Consumer<B> partialApply(A a) {
		return (b) -> apply(a,b);
	}
	
	default <C> TriConsumer<A,B,C> unApply(Class<C> clazz) {
		return (a,b,c) -> apply(a,b);
	}
	
	default BiConsumer<B,A> rotate() {
		return (b,a) -> apply(a,b);
	}
	
	default BiConsumer<A, B> discardReturn() {
		return this;
	}
}
