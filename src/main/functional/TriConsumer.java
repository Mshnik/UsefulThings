package functional;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<A,B,C> extends TriFuncShell<A,B,C> {
	void apply(A a, B b, C c);
	
	default void accept(A a, B b, C c){
		apply(a,b,c);
	}
	
	default Unit partialApply(A a, B b, C c) {
		return () -> apply(a, b, c);
	}
	
	default Consumer<C> partialApply(A a, B b) {
		return (c) -> apply(a,b,c);
	}
	
	default BiConsumer<B, C> partialApply(A a) {
		return (b, c) -> apply(a,b,c);
	}
	
	default TriConsumer<C,A,B> rotate() {
		return (c,a,b) -> apply(a,b,c);
	}
	
	default TriConsumer<A, B, C> discardReturn() {
		return this;
	}
	
	default TriConsumer<A,B,C> andThen(TriConsumer<? super A, ? super B, ? super C> after) {
    Objects.requireNonNull(after);
    return (a, b, c) -> {
        apply(a, b, c);
        after.apply(a, b, c);
    };
	}
}