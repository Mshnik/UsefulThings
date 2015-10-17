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

  default Unit butFirst(Unit before) {
    return () -> {
      before.apply();
      apply();
    };
  }

	default Unit andThen(Unit next) {
    return () -> {
      apply();
      next.apply();
    };
  }

	default Supplier<Unit> asResult() {
    return () -> this;
  }
}
