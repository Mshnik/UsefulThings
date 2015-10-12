package functional;

public interface QuadFuncShell<A, B, C, D> {


  abstract UnitFuncShell partialApply(A a, B b, C c, D d);

  abstract SingleFuncShell<D> partialApply(A a, B b, C c);

  abstract BiFuncShell<C, D> partialApply(A a, B b);

  abstract TriFuncShell<B, C, D> partialApply(A a);

  abstract QuadConsumer<A, B, C, D> discardReturn();

}
