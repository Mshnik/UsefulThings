package functional.impl.ex;

import functional._0ArgShell;
import functional._ExShell;
import functional._NonReturnShell;
import functional.impl.Supplier;
import functional.impl.Unit;
import functional.impl.Consumer1;


@FunctionalInterface
public interface UnitEx extends _ExShell, _0ArgShell, _NonReturnShell {

  void apply() throws Throwable;

  default Unit withHandler(Consumer1<Throwable> handler) {
    return () -> {
      try {
        apply();
      } catch(Throwable t) {
        handler.apply(t);
      }
    };
  }

  default Unit ignoreThrowable() {
    return withHandler(DO_NOTHING);
  }

  default UnitEx discardReturn() {
    return this;
  }

  default SupplierEx<Void> supplyNothing() {
    return () -> {
      apply();
      return null;
    };
  }

  default UnitEx butFirst(UnitEx before) {
    return () -> {
      before.apply();
      apply();
    };
  }

  default UnitEx butFirst(Unit before) {
    return () -> {
      before.apply();
      apply();
    };
  }

  default UnitEx andThen(UnitEx next) {
    return () -> {
      apply();
      next.apply();
    };
  }

  default UnitEx andThen(Unit next) {
    return () -> {
      apply();
      next.apply();
    };
  }
}
