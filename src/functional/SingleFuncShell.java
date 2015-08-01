package functional;

public interface SingleFuncShell<A> extends FuncShell {
	
	abstract UnitFuncShell partialApply(A a);
	
	abstract <B> BiFuncShell<A, B> unApply(Class<B> clazz);
	
	abstract Consumer<A> discardReturn();
}
