package functional;

public interface UnitFuncShell extends FuncShell {

	abstract <A> SingleFuncShell<A> unApply(Class<A> clazz);
	
	abstract Unit asUnit();
}
