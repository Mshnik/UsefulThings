package functional;

@FunctionalInterface
public interface Unit extends FuncShell {
	void apply();
	
	default <A> Consumer<A> unApply(Class<A> clazz) {
		return (a) -> apply();
	}
}
