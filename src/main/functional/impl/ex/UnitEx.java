package functional.impl.ex;

import functional._ExShell;
import functional.impl.Supplier;
import functional.impl.Unit;
import functional.impl.Consumer1;


@FunctionalInterface
public interface UnitEx extends _ExShell {

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

  default Unit withNoHandler() {
    return withHandler(DO_NOTHING);
  }

  default Unit asUnit() {
    return withHandler(DO_NOTHING);
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

  default UnitEx andThen(UnitEx next) {
    return () -> {
      apply();
      next.apply();
    };
  }

  default Supplier<? extends UnitEx> asResult() {
    return () -> this;
  }
}
