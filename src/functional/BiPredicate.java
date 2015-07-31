package functional;

public interface BiPredicate<A, B> extends FuncShell {
	boolean apply(A a, B b);
	
	default Supplier<Boolean> partialApply(A a, B b) {
		return () -> apply(a, b);
	}
	
	default Predicate<B> partialApply(A a) {
		return (b) -> apply(a,b);
	}
	
	default <C> TriPredicate<A,B,C> unApply(Class<C> clazz) {
		return (a,b,c) -> apply(a,b);
	}
	
	default BiPredicate<B,A> rotate() {
		return (b,a) -> apply(a,b);
	}
	
	default BiPredicate<A, B> and(BiPredicate<A, B> other) {
		return (a, b) -> apply(a, b) && other.apply(a, b);
	}
	
	default BiPredicate<A, B> or(BiPredicate<A, B> other) {
		return (a, b) -> apply(a, b) || other.apply(a, b);
	}
	
	default BiPredicate<A, B> nand(BiPredicate<A, B> other) {
		return (a, b) -> ! (apply(a, b) && other.apply(a, b));
	}
	
	default BiPredicate<A, B> nor(BiPredicate<A, B> other) {
		return (a, b) -> ! (apply(a, b) || other.apply(a, b));
	}
	
	default BiPredicate<A, B> xor(BiPredicate<A, B> other) {
		return (a, b) -> apply(a, b) ^ other.apply(a, b);
	}
	
	default BiPredicate<A, B> xnor(BiPredicate<A, B> other) {
		return (a, b) -> ! (apply(a, b) ^ other.apply(a, b));
	}
}
