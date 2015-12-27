package functional.impl;

import functional._0ArgShell;
import functional._NonExShell;
import functional._NonReturnShell;
import functional.impl.ex.SupplierEx;
import functional.impl.ex.UnitEx;

@FunctionalInterface
public interface Unit extends _0ArgShell, _NonReturnShell, UnitEx, _NonExShell {
	void apply();
	
	default Unit discardReturn() {
		return this;
	}

	default SupplierEx<Void> supplyNothing() {
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

	default Supplier<? extends Unit> asResult() {
    return () -> this;
  }
}
