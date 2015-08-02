package functional;

public interface BiFuncShell<A, B> extends FuncShell {

	abstract UnitFuncShell partialApply(A a, B b);
	
	abstract SingleFuncShell<B> partialApply(A a);
	
	abstract BiConsumer<A, B> discardReturn();
}
