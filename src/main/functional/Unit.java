package functional;

@FunctionalInterface
public interface Unit extends UnitFuncShell {
	void apply();
	
	default Unit asUnit() {
		return this;
	}
}
