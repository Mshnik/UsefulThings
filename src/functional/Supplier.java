package functional;

@FunctionalInterface
public interface Supplier<R> extends java.util.function.Supplier<R>, UnitFuncShell {
	R apply();
	
	default R get() {
		return apply();
	}
	
	default Unit asUnit() {
		return () -> apply();
	}
}
