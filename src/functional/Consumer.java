package functional;

@FunctionalInterface
public interface Consumer<A> extends SingleFuncShell<A> {
	void apply(A a);
	
	default Unit partialApply(A a) {
		return () -> apply(a);
	}
	
	default <B> BiConsumer<A,B> unApply(Class<B> clazz) {
		return (a,b) -> apply(a);
	}
	
	default Consumer<A> discardReturn() {
		return this;
	}
}
