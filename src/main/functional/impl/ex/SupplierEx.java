package functional.impl.ex;

import functional._ExShell;
import functional.impl.Function1;
import functional.impl.Supplier;
import functional.impl.Unit;

@FunctionalInterface
public interface SupplierEx<R> extends _ExShell {
  R apply() throws Throwable;

  default Supplier<R> withHandler(Function1<Throwable, R> handler) {
    return () -> {
      try {
        return apply();
      } catch(Throwable t) {
        return handler.apply(t);
      }
    };
  }

  default Supplier<R> withNoHandler(R ifExceptionThrown) {
    return () -> {
      try {
        return apply();
      } catch(Throwable t) {
        return ifExceptionThrown;
      }
    };
  }

  default R get(R ifException) {
    try {
      return apply();
    } catch(Throwable t) {
      return ifException;
    }
  }

  default UnitEx asUnit() {
    return () -> apply();
  }

  default SupplierEx<R> butFirst(Unit before) {
    return () -> {
      before.apply();
      return apply();
    };
  }
}
