package functional;

@FunctionalInterface
public interface Predicate<A> extends FuncShell {
	boolean apply(A a);
	
	default Supplier<Boolean> partialApply(A a) {
		return () -> apply(a);
	}
	
	default <B> BiPredicate<A,B> unApply(Class<B> clazz) {
		return (a,b) -> apply(a);
	}
	
	default Predicate<A> and(Predicate<A> other) {
		return (a) -> apply(a) && other.apply(a);
	}
	
	default Predicate<A> or(Predicate<A> other) {
		return (a) -> apply(a) || other.apply(a);
	}
	
	default Predicate<A> nand(Predicate<A> other) {
		return (a) -> ! (apply(a) && other.apply(a));
	}
	
	default Predicate<A> nor(Predicate<A> other) {
		return (a) -> ! (apply(a) || other.apply(a));
	}
	
	default Predicate<A> xor(Predicate<A> other) {
		return (a) -> apply(a) ^ other.apply(a);
	}
	
	default Predicate<A> xnor(Predicate<A> other) {
		return (a) -> ! (apply(a) ^ other.apply(a));
	}
}
