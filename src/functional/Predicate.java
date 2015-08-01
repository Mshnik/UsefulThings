package functional;

import java.util.Objects;

@FunctionalInterface
public interface Predicate<A> extends java.util.function.Predicate<A>, SingleFuncShell<A> {
	boolean apply(A a);
	
	default boolean test(A a) {
		return apply(a);
	}
	
	default Supplier<Boolean> partialApply(A a) {
		return () -> apply(a);
	}
	
	default Consumer<A> discardReturn() {
		return (a) -> apply(a);
	}
	
	default Predicate<A> nand(Predicate<? super A> other) {
    Objects.requireNonNull(other);
		return (a) -> ! (apply(a) && other.apply(a));
	}
	
	default Predicate<A> nor(Predicate<? super A> other) {
    Objects.requireNonNull(other);
		return (a) -> ! (apply(a) || other.apply(a));
	}
	
	default Predicate<A> xor(Predicate<? super A> other) {
    Objects.requireNonNull(other);
		return (a) -> apply(a) ^ other.apply(a);
	}
	
	default Predicate<A> xnor(Predicate<? super A> other) {
    Objects.requireNonNull(other);
		return (a) -> ! (apply(a) ^ other.apply(a));
	}
}