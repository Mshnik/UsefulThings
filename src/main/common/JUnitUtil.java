package common;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import functional.*;

import static org.junit.Assert.*;

public class JUnitUtil {

  private JUnitUtil() {
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
      fail(request + " did not fail");
    }
    if (!expectedException.isAssignableFrom(ex.getClass())) {
      fail(request + " failed, but with exception " + ex + ". Expected " + expectedException);
    }
  }

  /**
   * Tests that the given request fails.
   * Throws an assertion exception if it does not fail
   */
  public static <T> void shouldFail(Supplier<T> request) {
    shouldFail(request, Throwable.class);
  }

  /**
   * Tests that the given request fails with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, E extends Throwable> void shouldFail(Supplier<T> request, Class<E> expectedException) {
    boolean got = false;
    Exception ex = null;
    T t = null;
    try {
      t = request.get();
      got = true;
    } catch (Exception e) {
      ex = e;
    }

    if (got) {
      fail(request + " did not fail. Got " + t);
    }
    if (!expectedException.isAssignableFrom(ex.getClass())) {
      fail(request + " failed, but with exception " + ex + ". Expected " + expectedException);
    }
  }

  /**
   * Tests that the given request fails on the given input.
   * Throws an assertion exception if it does not fail
   */
  public static <T> void shouldFail(Consumer<T> request, T arg) {
    shouldFail(request, arg, Throwable.class);
  }

  /**
   * Tests that the given request fails on the given input with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, E extends Throwable> void shouldFail(Consumer<T> request, T arg, Class<E> expectedException) {
    boolean got = false;
    Exception ex = null;
    try {
      request.accept(arg);
      got = true;
    } catch (Exception e) {
      ex = e;
    }

    if (got) {
      fail(request + " did not fail. Got");
    }
    if (!expectedException.isAssignableFrom(ex.getClass())) {
      fail(request + " failed, but with exception " + ex + ". Expected " + expectedException);
    }
  }

  /**
   * Tests that the given request fails on the given input
   * Throws an assertion exception if it does not fail
   */
  public static <T, R> void shouldFailOn(Function<T, R> request, T arg) {
    shouldFailOn(request, arg, Throwable.class);
  }

  /**
   * Tests that the given request fails on the given input with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, E extends Throwable> void shouldFailOn(Function<T, R> request, T arg, Class<E> expectedException) {
    boolean got = false;
    Exception ex = null;
    R r = null;
    try {
      r = request.apply(arg);
      got = true;
    } catch (Exception e) {
      ex = e;
    }

    if (got) {
      fail(request + " did not fail. Got " + r);
    }
    if (!expectedException.isAssignableFrom(ex.getClass())) {
      fail(request + " failed, but with exception " + ex + ". Expected " + expectedException);
    }
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R> void shouldFail(BiConsumer<T, R> request, T arg1, R arg2) {
    shouldFail(request, arg1, arg2, Throwable.class);
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, E extends Throwable> void shouldFail(BiConsumer<T, R> request, T arg1, R arg2, Class<E> expectedException) {
    boolean got = false;
    Exception ex = null;
    try {
      request.accept(arg1, arg2);
      got = true;
    } catch (Exception e) {
      ex = e;
    }

    if (got) {
      fail(request + " did not fail");
    }
    if (!expectedException.isAssignableFrom(ex.getClass())) {
      fail(request + " failed, but with exception " + ex + ". Expected " + expectedException);
    }
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R, S> void shouldFailOn(BiFunction<T, R, S> request, T arg1, R arg2) {
    shouldFailOn(request, arg1, arg2, Throwable.class);
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, S, E extends Throwable> void shouldFailOn(BiFunction<T, R, S> request, T arg1, R arg2, Class<E> expectedException) {
    boolean got = false;
    Exception ex = null;
    S s = null;
    try {
      s = request.apply(arg1, arg2);
      got = true;
    } catch (Exception e) {
      ex = e;
    }

    if (got) {
      fail(request + " did not fail. Got " + s);
    }
    if (!expectedException.isAssignableFrom(ex.getClass())) {
      fail(request + " failed, but with exception " + ex + ". Expected " + expectedException);
    }
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R, S> void shouldFail(TriConsumer<T, R, S> request, T arg1, R arg2, S arg3) {
    shouldFail(request, arg1, arg2, arg3, Throwable.class);
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, S, E extends Throwable> void shouldFail(TriConsumer<T, R, S> request,
                                                               T arg1, R arg2, S arg3, Class<E> expectedException) {
    boolean got = false;
    Exception ex = null;
    try {
      request.apply(arg1, arg2, arg3);
      got = true;
    } catch (Exception e) {
      ex = e;
    }

    if (got) {
      fail(request + " did not fail");
    }
    if (!expectedException.isAssignableFrom(ex.getClass())) {
      fail(request + " failed, but with exception " + ex + ". Expected " + expectedException);
    }
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R, S, Q> void shouldFailOn(TriFunction<T, R, S, Q> request, T arg1, R arg2, S arg3) {
    shouldFailOn(request, arg1, arg2, arg3, Throwable.class);
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, S, Q, E extends Throwable> void shouldFailOn(TriFunction<T, R, S, Q> request,
                                                                    T arg1, R arg2, S arg3, Class<E> expectedException) {
    boolean got = false;
    Exception ex = null;
    Q q = null;
    try {
      q = request.apply(arg1, arg2, arg3);
      got = true;
    } catch (Exception e) {
      ex = e;
    }

    if (got) {
      fail(request + " did not fail. Got " + q);
    }
    if (!expectedException.isAssignableFrom(ex.getClass())) {
      fail(request + " failed, but with exception " + ex + ". Expected " + expectedException);
    }
  }

}
