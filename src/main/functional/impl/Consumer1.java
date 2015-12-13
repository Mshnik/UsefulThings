package functional.impl;

import functional._1ArgShell;
import functional._NonReturnShell;
import functional.impl.ex.Consumer1Ex;

@FunctionalInterface
public interface Consumer1<A> extends java.util.function.Consumer<A>, _1ArgShell<A>, _NonReturnShell {
	void apply(A a);
	
	default void accept(A a) {
		apply(a);
	}
	
	default Unit partialApply(A a) {
		return () -> apply(a);
	}

  default Unit lazyApply(Supplier<A> aSupplier) {
    return () -> apply(aSupplier.apply());
  }

	default Consumer1<A> discardReturn() {
		return this;
	}

	default Consumer1<A> butFirst(Unit before) {
		return (a) -> {
			before.apply();
			apply(a);
		};
	}
}
