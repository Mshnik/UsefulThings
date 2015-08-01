package functional;

public interface TriFuncShell<A, B, C> {

	abstract UnitFuncShell partialApply(A a, B b, C c);

	abstract SingleFuncShell<C> partialApply(A a, B b);
	
	abstract BiFuncShell<B, C> partialApply(A a);
	
	abstract TriConsumer<A, B, C> discardReturn();
}
