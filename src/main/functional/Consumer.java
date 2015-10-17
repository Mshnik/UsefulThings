package functional;

@FunctionalInterface
public interface Consumer<A> extends java.util.function.Consumer<A>, SingleFuncShell<A> {
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

	default Consumer<A> discardReturn() {
		return this;
	}

	default Consumer<A> butFirst(Unit before) {
		return (a) -> {
			before.apply();
			apply(a);
		};
	}
}
