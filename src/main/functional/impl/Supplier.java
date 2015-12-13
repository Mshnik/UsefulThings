package functional.impl;

import functional._0ArgShell;
import functional._ReturnShell;

@FunctionalInterface
public interface Supplier<R> extends java.util.function.Supplier<R>, _0ArgShell, _ReturnShell<R> {
  R apply();

  default R get() {
    return apply();
  }

  default Unit asUnit() {
    return () -> apply();
  }

  default Supplier<R> butFirst(Unit before) {
    return () -> {
      before.apply();
      return apply();
    };
  }

  static <T> Supplier<T> supply(T t) {
    return () -> t;
  }
}
