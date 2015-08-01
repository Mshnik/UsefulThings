package functional;

@FunctionalInterface
public interface Supplier<R> extends UnitFuncShell {
	R apply();
	
	default <A> Function<A,R> unApply(Class<A> clazz) {
		return (a) -> apply();
	}
	
	default Unit asUnit() {
		return () -> apply();
	}
}
