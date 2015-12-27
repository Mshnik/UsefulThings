package functional;

import functional.impl.Consumer1;

import java.util.function.Function;

public interface _ExShell {

  public static Consumer1<Throwable> DO_NOTHING = (t) -> {};

  //_NonExShell withHandler(_1ArgShell<Throwable> handler);

  _ExShell discardReturn();
}
