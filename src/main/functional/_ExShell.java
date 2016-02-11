package functional;

import functional.impl.Consumer1;
import functional.impl.Unit;
import functional.impl.ex.UnitEx;

import java.util.function.Function;

public interface _ExShell {

  public static Consumer1<Throwable> DO_NOTHING = (t) -> {};

  _ExShell discardReturn();

  _ExShell butFirst(Unit before);

  _ExShell andThen(Unit after);
}
