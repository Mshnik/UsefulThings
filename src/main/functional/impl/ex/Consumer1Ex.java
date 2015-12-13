package functional.impl.ex;

import functional._ExShell;
import functional._NonReturnShell;
import functional.impl.Consumer1;
import functional.impl.Supplier;
import functional.impl.Unit;

@FunctionalInterface
public interface Consumer1Ex<A> extends _ExShell, _NonReturnShell {
	void apply(A a) throws Throwable;

	default Consumer1<A> withHandler(Consumer1<Throwable> handler) {
		return (a) -> {
			try {
        apply(a);
			} catch(Throwable t) {
        handler.apply(t);
			}
		};
	}

	default Consumer1<A> withNoHandler() {
    return withHandler(DO_NOTHING);
  }
	
	default UnitEx partialApply(A a) {
		return () -> apply(a);
	}

  default UnitEx lazyApply(Supplier<A> aSupplier) {
    return () -> apply(aSupplier.apply());
  }

	default Consumer1Ex<A> discardReturn() {
		return this;
	}

	default Consumer1Ex<A> butFirst(Unit before) {
		return (a) -> {
			before.apply();
			apply(a);
		};
	}
}
