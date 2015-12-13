package functional.impl;

import functional._0ArgShell;
import functional._NonReturnShell;

@FunctionalInterface
public interface Unit extends _0ArgShell, _NonReturnShell {
	void apply();
	
	default Unit asUnit() {
		return this;
	}

	default Supplier<Void> supplyNothing() {
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
