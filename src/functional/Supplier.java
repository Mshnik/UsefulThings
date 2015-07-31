package functional;

@FunctionalInterface
public interface Supplier<R> extends FuncShell {
	R apply();
	
	default <A> Function<A,R> unApply(Class<A> clazz) {
		return (a) -> apply();
	}
}
