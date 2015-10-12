package common;

import functional.*;
import functional.BiConsumer;
import functional.Consumer;
import org.junit.Assert;

import java.util.function.*;

import static org.junit.Assert.*;
import static functional.FunctionalUtil.*;

public class JUnitUtil {

  private JUnitUtil() {}

  public static Unit failFunc() {
    return Assert::fail;
  }

  public static Unit failFunc(String message) {
    Consumer<String> f = Assert::fail;
    return f.partialApply(message);
  }

  public static Unit assertTrueFunc(boolean condition) {
    Consumer<Boolean> f = Assert::assertTrue;
    return f.partialApply(condition);
  }

  public static Unit assertTrueFunc(String message, boolean condition) {
    BiConsumer<String, Boolean> f = Assert::assertTrue;
    return f.partialApply(message, condition);
  }

  public static Unit assertFalseFunc(boolean condition) {
    Consumer<Boolean> f = Assert::assertFalse;
    return f.partialApply(condition);
  }

  public static Unit assertFalseFunc(String message, boolean condition) {
    BiConsumer<String, Boolean> f = Assert::assertFalse;
    return f.partialApply(message, condition);
  }

  public static Unit assertEqualsFunc(Object expected, Object actual) {
    BiConsumer<Object, Object> f = Assert::assertEquals;
    return f.partialApply(expected, actual);
  }

  public static Unit assertEqualsFunc(String message, Object expected, Object actual) {
    TriConsumer<String, Object, Object> f = Assert::assertEquals;
    return f.partialApply(message, expected, actual);
  }

  /** Runs each of the unit functions given, and returns the count of them that do not
   * throw assertion errors. (That pass, in other words).
   * @param assertsToTest - an array of the Units to call.
   * @return
   */
  public static int testAll(Unit... assertsToTest) {
    if (assertsToTest == null || assertsToTest.length == 0) {
      return 0;
    }

    int passedCount = 0;
    for (Unit u : assertsToTest) {
      try{
        u.apply();
        passedCount++;
      } catch(AssertionError e){}
    }
    return passedCount;
  }

  /**
   * Tests that the given request fails.
   * Throws an assertion exception if it does not fail
   */
  public static void shouldFail(Unit request) {
    shouldFail(request, Throwable.class);
  }

  /**
   * Tests that the given request fails with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <E extends Throwable> void shouldFail(Unit request, Class<E> expectedException) {
    boolean got = false;
    Exception ex = null;
    try {
      request.apply();
      got = true;
    } catch (Exception e) {
      ex = e;
    }

    if (got) {
      fail("method call did not result in exception");
    }
    if (!expectedException.isAssignableFrom(ex.getClass())) {
      fail("method call failed, but with exception " + ex + ". Expected " + expectedException);
    }
  }

  /**
   * Tests that the given request fails.
   * Throws an assertion exception if it does not fail
   */
  public static <T> void shouldFail(java.util.function.Supplier<T> request) {
    shouldFail(migrate(request).asUnit());
  }

  /**
   * Tests that the given request fails with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, E extends Throwable> void shouldFail(java.util.function.Supplier<T> request, Class<E> expectedException) {
    shouldFail(migrate(request).asUnit(), expectedException);
  }

  /**
   * Tests that the given request fails on the given input.
   * Throws an assertion exception if it does not fail
   */
  public static <T> void shouldFail(java.util.function.Consumer<T> request, T arg) {
    shouldFail(migrate(request).partialApply(arg));
  }

  /**
   * Tests that the given request fails on the given input with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, E extends Throwable> void shouldFail(java.util.function.Consumer<T> request, Class<E> expectedException, T arg) {
    shouldFail(migrate(request).partialApply(arg), expectedException);
  }

  /**
   * Tests that the given request fails on the given input
   * Throws an assertion exception if it does not fail
   */
  public static <T, R> void shouldFail(java.util.function.Function<T, R> request, T arg) {
    shouldFail(migrate(request).partialApply(arg).asUnit());
  }

  /**
   * Tests that the given request fails on the given input with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, E extends Throwable> void shouldFail(java.util.function.Function<T, R> request, Class<E> expectedException, T arg) {
    shouldFail(migrate(request).partialApply(arg).asUnit(), expectedException);
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R> void shouldFail(java.util.function.BiConsumer<T, R> request, T arg1, R arg2) {
    shouldFail(migrate(request).partialApply(arg1, arg2).asUnit());
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, E extends Throwable> void shouldFail(java.util.function.BiConsumer<T, R> request, Class<E> expectedException, T arg1, R arg2) {
    shouldFail(migrate(request).partialApply(arg1, arg2).asUnit(), expectedException);
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R, S> void shouldFail(java.util.function.BiFunction<T, R, S> request, T arg1, R arg2) {
    shouldFail(migrate(request).partialApply(arg1, arg2).asUnit());
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, S, E extends Throwable> void shouldFail(java.util.function.BiFunction<T, R, S> request, Class<E> expectedException, T arg1, R arg2) {
    shouldFail(migrate(request).partialApply(arg1, arg2).asUnit(), expectedException);
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R, S> void shouldFail(TriConsumer<T, R, S> request, T arg1, R arg2, S arg3) {
    shouldFail(request.partialApply(arg1, arg2, arg3).asUnit());
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, S, E extends Throwable> void shouldFail(TriConsumer<T, R, S> request, Class<E> expectedException,
                                                               T arg1, R arg2, S arg3) {
    shouldFail(request.partialApply(arg1, arg2, arg3).asUnit(), expectedException);
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R, S, U> void shouldFail(QuadConsumer<T, R, S, U> request, T arg1, R arg2, S arg3, U arg4) {
    shouldFail(request.partialApply(arg1, arg2, arg3, arg4).asUnit());
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, S, U, E extends Throwable> void shouldFail(QuadConsumer<T, R, S, U> request, Class<E> expectedException,
                                                               T arg1, R arg2, S arg3, U arg4) {
    shouldFail(request.partialApply(arg1, arg2, arg3, arg4).asUnit(), expectedException);
  }

}
