package functional.impl;

import functional._0ArgShell;
import functional._ReturnShell;
import functional.impl.ex.SupplierEx;

@FunctionalInterface
public interface Supplier<R> extends java.util.function.Supplier<R>, _0ArgShell, _ReturnShell<R>, SupplierEx {
  R apply();

  default R get() {
    return apply();
  }

  default Unit asUnit() {
    return () -> apply();
  }

  default Supplier<? extends R> butFirst(Unit before) {
    return () -> {
      before.apply();
      return apply();
    };
  }
}
