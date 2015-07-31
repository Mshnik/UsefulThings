package functional;

@FunctionalInterface
public interface Consumer<A> extends FuncShell {
	void apply(A a);
	
	default Unit partialApply(A a) {
		return () -> apply(a);
	}
	
	default <B> BiConsumer<A,B> unApply(Class<B> clazz) {
		return (a,b) -> apply(a);
	}
}
