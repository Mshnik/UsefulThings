package functional;

import functional.impl.Consumer1;

public interface _ReturnShell<R> {

  _NonReturnShell discardReturn();

//  _ReturnShell<R> andThenWith(Consumer1<R> after);
}
