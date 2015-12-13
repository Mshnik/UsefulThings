package functional.impl.ex;

import functional._1ArgShell;
import functional._ExShell;
import functional._ReturnShell;
import functional.impl.Consumer1;
import functional.impl.Function1;
import functional.impl.Supplier;
import functional.impl.Unit;

import java.util.Objects;

@FunctionalInterface
public interface Function1Ex<A, R> extends _ExShell {
  R apply(A a) throws Throwable;

  default Function1<A, R> withHandler(Function1<Throwable, R> handler) {
    return (a) -> {
      try {
        return apply(a);
      } catch(Throwable t) {
        return handler.apply(t);
      }
    };
  }

  default Function1<A, R> withNoHandler(R ifExceptionThrown) {
    return (a) -> {
      try {
        return apply(a);
      } catch(Throwable t) {
        return ifExceptionThrown;
      }
    };
  }

  default SupplierEx<R> partialApply(A a) {
    return () -> apply(a);
  }

  default SupplierEx<R> lazyApply(Supplier<A> aSupplier) {
    return () -> apply(aSupplier.apply());
  }

  default Consumer1Ex<A> discardReturn() {
    return (a) -> apply(a);
  }

  default <V> Function1Ex<V, R> compose(Function1Ex<? super V, ? extends A> before) {
    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
  }

  default <V> Function1Ex<A, V> andThen(Function1Ex<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (a) -> after.apply(apply(a));
  }

  default Function1Ex<A, R> butFirst(UnitEx before) {
    return (a) -> {
      before.apply();
      return apply(a);
    };
  }
}

