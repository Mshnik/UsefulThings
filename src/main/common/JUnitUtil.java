package common;

import functional.impl.*;
import functional.impl.ex.*;
import org.junit.Assert;

import java.util.Iterator;

//TODO - SPEC
public class JUnitUtil {

  private JUnitUtil() {}

  /** Error term for comparing floats and doubles. If the diff is less than this, they are considered equal */
  private static float ERR_TERM = 0.00001f;

  public static void fail() {
    Assert.fail();
  }

  public static void fail(String message) {
    Assert.fail(message);
  }

  public static void assertTrue(String message, boolean condition) {
    if(!condition) {
      fail(message);
    }
  }

  public static void assertTrue(boolean condition) {
    assertTrue(null, condition);
  }

  public static void assertFalse(String message, boolean condition) {
    assertTrue(message, !condition);
  }

  public static void assertFalse(boolean condition) {
    assertFalse(null, condition);
  }

  public static void assertEquals(Object expected, Object actual) {
    assertEquals((String)null, expected, actual);
  }


  /** A single assertEquals function that handles all of the different cases of
   * JUnit assertEquals
   */
  public static void assertEquals(String message, Object expected, Object actual) {
    if(expected == null && actual == null) {
      //Awesome! Nulls match.
      return;
    } else if(expected == null || actual == null) {
      fail(message);
    } else if (expected instanceof Double && actual instanceof Double) {
      Assert.assertEquals(message, ERR_TERM, (Double)expected, (Double)actual);
    } else if (expected instanceof Float && actual instanceof Float) {
      Assert.assertEquals(message, ERR_TERM, (Float)expected, (Float)actual);
    } else if (expected instanceof Object[] && actual instanceof Object[]) {
      Assert.assertArrayEquals(message, (Object[]) expected, (Object[]) actual);
    } else if (expected instanceof int[] && actual instanceof int[]) {
      Assert.assertArrayEquals(message, (int[]) expected, (int[]) actual);
    } else if (expected instanceof boolean[] && actual instanceof boolean[]) {
      Assert.assertArrayEquals(message, (boolean[]) expected, (boolean[]) actual);
    } else if (expected instanceof short[] && actual instanceof short[]) {
      Assert.assertArrayEquals(message, (short[]) expected, (short[]) actual);
    } else if (expected instanceof long[] && actual instanceof long[]) {
      Assert.assertArrayEquals(message, (long[]) expected, (long[]) actual);
    } else if (expected instanceof byte[] && actual instanceof byte[]) {
      Assert.assertArrayEquals(message, (byte[]) expected, (byte[]) actual);
    } else if (expected instanceof char[] && actual instanceof char[]) {
      Assert.assertArrayEquals(message, (char[]) expected, (char[]) actual);
    } else if (expected instanceof float[] && actual instanceof float[]) {
      Assert.assertArrayEquals(message, (float[]) expected, (float[]) actual, ERR_TERM);
    } else if (expected instanceof double[] && actual instanceof double[]) {
      Assert.assertArrayEquals(message, (double[]) expected, (double[]) actual, ERR_TERM);
    } else if (expected instanceof Iterator<?> && actual instanceof Iterator<?>) {
      Iterator<?> i1 = (Iterator<?>)expected;
      Iterator<?> i2 = (Iterator<?>)actual;
      while(i1.hasNext() && i2.hasNext()) {
        assertEquals(message, i1.next(), i2.next());
      }
      assertTrue(! i1.hasNext() && ! i2.hasNext());
    } else {
      Assert.assertEquals(message, expected, actual);
    }
  }

  /** Returns a Unit that causes an assertion failure */
  public static Unit failFunc() {
    return Assert::fail;
  }

  /** Returns a Unit that causes an assertion failure with the given message */
  public static Unit failFunc(String message) {
    Consumer1<String> f = Assert::fail;
    return f.partialApply(message);
  }

  /** Returns a Unit that asserts that the given condition is true */
  public static Unit assertTrueFunc(boolean condition) {
    Consumer1<Boolean> f = Assert::assertTrue;
    return f.partialApply(condition);
  }

  /** Returns a Unit that asserts that the given condition is true with the given message */
  public static Unit assertTrueFunc(String message, boolean condition) {
    Consumer2<String, Boolean> f = Assert::assertTrue;
    return f.partialApply(message, condition);
  }

  /** Returns a Unit that asserts that the given condition is false */
  public static Unit assertFalseFunc(boolean condition) {
    Consumer1<Boolean> f = Assert::assertFalse;
    return f.partialApply(condition);
  }

  /** Returns a Unit that asserts that the given condition is false with the given message */
  public static Unit assertFalseFunc(String message, boolean condition) {
    Consumer2<String, Boolean> f = Assert::assertFalse;
    return f.partialApply(message, condition);
  }

  /** Returns a Unit that asserts that the two objects are equal */
  public static Unit assertEqualsFunc(Object expected, Object actual) {
    Consumer2<Object, Object> f = Assert::assertEquals;
    return f.partialApply(expected, actual);
  }

  /** Returns a Unit that asserts that the two objects are equal with the given message */
  public static Unit assertEqualsFunc(String message, Object expected, Object actual) {
    Consumer3<String, Object, Object> f = Assert::assertEquals;
    return f.partialApply(message, expected, actual);
  }

  /** Call the given supplier, and assert that it is true */
  public static void callAndAssetTrue(Supplier<Boolean> conditionSupplier) {
    assertTrue(conditionSupplier.apply());
  }

  /** Call the given supplier, and assert that it is true with the given message */
  public static void callAndAssetTrue(String message, Supplier<Boolean> conditionSupplier) {
    assertTrue(message, conditionSupplier.apply());
  }

  /** Call the given supplier, and assert that it is false */
  public static void callAndAssetFalse(Supplier<Boolean> conditionSupplier) {
    assertFalse(conditionSupplier.apply());
  }

  /** Call the given supplier, and assert that it is false with the given message */
  public static void callAndAssetFalse(String message, Supplier<Boolean> conditionSupplier) {
    assertFalse(message, conditionSupplier.apply());
  }

  /** Call the given supplier, and assert that the two objects are equal */
  public static void callAndAssertEquals(Object expected, Supplier<Object> actualSupplier) {
    assertEquals(expected, actualSupplier.apply());
  }

  /** Call the given suppliers, and assert that the two objects are equal */
  public static void callAndAssertEquals(Supplier<Object> expectedSupplier, Supplier<Object> actualSupplier) {
    assertEquals(expectedSupplier.apply(), actualSupplier.apply());
  }

  /** Call the given supplier, and assert that the two objects are equal with the given message */
  public static void callAndAssertEquals(String message, Object expected, Supplier<Object> actualSupplier) {
    assertEquals(message, expected, actualSupplier.apply());
  }

  /** Call the given suppliers, and assert that the two objects are equal with the given message*/
  public static void callAndAssertEquals(String message, Supplier<Object> expectedSupplier, Supplier<Object> actualSupplier) {
    assertEquals(message, expectedSupplier.apply(), actualSupplier.apply());
  }

  /** Returns a Unit that asserts that the condition from the given supplier is true */
  public static Unit callAndAssertTrueFunc(Supplier<Boolean> conditionSupplier) {
    Consumer1<Boolean> f = Assert::assertTrue;
    return f.lazyApply(conditionSupplier);
  }

  /** Returns a Unit that asserts that the given condition is true with the given message */
  public static Unit callAndAssertTrueFunc(String message, Supplier<Boolean> conditionSupplier) {
    Consumer2<String, Boolean> f = Assert::assertTrue;
    return f.partialLazyApply(message, conditionSupplier);
  }

  /** Returns a Unit that asserts that the given condition is false */
  public static Unit callAndAssertFalseFunc(Supplier<Boolean> conditionSupplier) {
    Consumer1<Boolean> f = Assert::assertFalse;
    return f.lazyApply(conditionSupplier);
  }

  /** Returns a Unit that asserts that the given condition is false with the given message */
  public static Unit callAndAssertFalseFunc(String message, Supplier<Boolean> conditionSupplier) {
    Consumer2<String, Boolean> f = Assert::assertFalse;
    return f.partialLazyApply(message, conditionSupplier);
  }

  /** Returns a Unit that asserts that the two objects are equal */
  public static Unit callAndAssertEqualsFunc(Object expected, Supplier<Object> actualSupplier) {
    Consumer2<Object, Object> f = Assert::assertEquals;
    return f.partialLazyApply(expected, actualSupplier);
  }

  /** Returns a Unit that asserts that the two objects are equal */
  public static Unit callAndAssertEqualsFunc(Supplier<Object> expectedSupplier, Supplier<Object> actualSupplier) {
    Consumer2<Object, Object> f = Assert::assertEquals;
    return f.lazyApply(expectedSupplier, actualSupplier);
  }

  /** Returns a Unit that asserts that the two objects are equal with the given message */
  public static Unit callAndAssertEqualsFunc(String message, Object expected, Supplier<Object> actualSupplier) {
    Consumer3<String, Object, Object> f = Assert::assertEquals;
    return f.partialLazyApply(message, expected, actualSupplier);
  }

  /** Returns a Unit that asserts that the two objects are equal with the given message */
  public static Unit callAndAssertEqualsFunc(String message, Supplier<Object> expectedSupplier, Supplier<Object> actualSupplier) {
    Consumer3<String, Object, Object> f = Assert::assertEquals;
    return f.partialLazyApply(message, expectedSupplier, actualSupplier);
  }

  /** Runs each of the unit functions given, and returns the count of them that do not
   * throw assertion errors. (That pass, in other words).
   * @param assertsToTest - an array of the Units to call
   * @return the count of asserts that pass
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
  public static void shouldFail(UnitEx request) {
    shouldFail(request, Throwable.class);
  }

  /**
   * Tests that the given request fails with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <E extends Throwable> void shouldFail(UnitEx request, Class<E> expectedException) {
    boolean got = false;
    Throwable ex = null;
    try {
      request.apply();
      got = true;
    } catch (Throwable e) {
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
  public static <T> void shouldFail(SupplierEx<T> request) {
    shouldFail(request.discardReturn());
  }

  /**
   * Tests that the given request fails with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, E extends Throwable> void shouldFail(SupplierEx<T> request, Class<E> expectedException) {
    shouldFail(request.discardReturn(), expectedException);
  }

  /**
   * Tests that the given request fails on the given input.
   * Throws an assertion exception if it does not fail
   */
  public static <T> void shouldFail(Consumer1Ex<T> request, T arg) {
    shouldFail(request.partialApply(arg));
  }

  /**
   * Tests that the given request fails on the given input with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, E extends Throwable> void shouldFail(Consumer1Ex<T> request, Class<E> expectedException, T arg) {
    shouldFail(request.partialApply(arg), expectedException);
  }

  /**
   * Tests that the given request fails on the given input
   * Throws an assertion exception if it does not fail
   */
  public static <T, R> void shouldFail(Function1Ex<T, R> request, T arg) {
    shouldFail(request.partialApply(arg).discardReturn());
  }

  /**
   * Tests that the given request fails on the given input with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, E extends Throwable> void shouldFail(Function1Ex<T, R> request, Class<E> expectedException, T arg) {
    shouldFail(request.partialApply(arg).discardReturn(), expectedException);
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R> void shouldFail(Consumer2Ex<T, R> request, T arg1, R arg2) {
    shouldFail(request.partialApply(arg1, arg2));
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, E extends Throwable> void shouldFail(Consumer2Ex<T, R> request, Class<E> expectedException, T arg1, R arg2) {
    shouldFail(request.partialApply(arg1, arg2), expectedException);
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R, S> void shouldFail(Function2Ex<T, R, S> request, T arg1, R arg2) {
    shouldFail(request.partialApply(arg1, arg2).discardReturn());
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, S, E extends Throwable> void shouldFail(Function2Ex<T, R, S> request, Class<E> expectedException, T arg1, R arg2) {
    shouldFail(request.partialApply(arg1, arg2).discardReturn(), expectedException);
  }

  /**
   * Tests that the given request fails on the given inputs
   * Throws an assertion exception if it does not fail
   */
  public static <T, R, S> void shouldFail(Consumer3Ex<T, R, S> request, T arg1, R arg2, S arg3) {
    shouldFail(request.partialApply(arg1, arg2, arg3));
  }

  /**
   * Tests that the given request fails on the given inputs with the given class of exception.
   * Throws an assertion exception if it does not fail, or fails with a different
   * class of exception
   */
  public static <T, R, S, E extends Throwable> void shouldFail(Consumer3Ex<T, R, S> request, Class<E> expectedException,
                                                               T arg1, R arg2, S arg3) {
    shouldFail(request.partialApply(arg1, arg2, arg3), expectedException);
  }
}
