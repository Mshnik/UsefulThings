package functional;

@FunctionalInterface
public interface Unit extends UnitFuncShell {
	void apply();
	
	default <A> Consumer<A> unApply(Class<A> clazz) {
		return (a) -> apply();
	}
	
	default Unit asUnit() {
		return this;
	}
}
