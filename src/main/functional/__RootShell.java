package functional;

import functional.impl.Unit;

public interface __RootShell {

  __RootShell butFirst(Unit before);

  __RootShell andThen(Unit after);

}
