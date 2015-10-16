package functional;

@FunctionalInterface
public interface Unit extends UnitFuncShell {
	void apply();
	
	default Unit asUnit() {
		return this;
	}

	default Supplier<Object> supplyNothing() {
		return () -> {
			apply();
			return null;
		};
	}

	default Supplier<Unit> asResult() {
    return () -> this;
  }
}
