package functional;

public interface SingleFuncShell<A> extends FuncShell {
	
	abstract UnitFuncShell partialApply(A a);
		
	abstract Consumer<A> discardReturn();
}
