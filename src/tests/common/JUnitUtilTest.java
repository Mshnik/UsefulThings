package common;

import functional.impl.Unit;
import functional.impl.ex.Consumer2Ex;
import org.junit.Assert;
import org.junit.Test;

import static common.JUnitUtil.assertEquals;
import static common.JUnitUtil.*;

public class JUnitUtilTest {

  private static void fails() throws RuntimeException {
    throw new RuntimeException();
  }

  private static void runsIfNotZero(int arg) throws RuntimeException {
    if (arg == 0){
      throw new RuntimeException();
    }
  }

  private static void runsIfNeitherZero(int arg1, int arg2) throws  RuntimeException {
    if (arg1 == 0 || arg2 == 0) {
      throw new RuntimeException();
    }
  }

  @Test
  public void testTestAll() {
    assertEquals(0, JUnitUtil.testAll((Unit[])null));
    assertEquals(0, JUnitUtil.testAll());

    assertEquals(0, JUnitUtil.testAll(Assert::fail));
    assertEquals(0, JUnitUtil.testAll(Assert::fail, Assert::fail));

    assertEquals(1, JUnitUtil.testAll(assertEqualsFunc(1,1)));
    assertEquals(2, JUnitUtil.testAll(assertEqualsFunc(1,1), assertEqualsFunc(2,2)));

    assertEquals(1, JUnitUtil.testAll(assertEqualsFunc(1,2), assertEqualsFunc(2,2)));
    assertEquals(2, JUnitUtil.testAll(assertEqualsFunc(1,1), assertEqualsFunc(2,2), assertEqualsFunc(2,3)));
  }

  @Test
  public void testShouldFail() {
    shouldFail(JUnitUtilTest::fails);
    shouldFail(JUnitUtilTest::runsIfNotZero, 0);
    shouldFail(JUnitUtilTest::runsIfNeitherZero, 0, 1);
    shouldFail(JUnitUtilTest::runsIfNeitherZero, 1, 0);

    shouldFail(JUnitUtilTest::fails, RuntimeException.class);
    shouldFail(JUnitUtilTest::runsIfNotZero, RuntimeException.class, 0);
    shouldFail(JUnitUtilTest::runsIfNeitherZero, RuntimeException.class, 0, 1);
    shouldFail(JUnitUtilTest::runsIfNeitherZero, RuntimeException.class, 1, 0);
  }

  @Test
  public void testAssertEquals() throws Exception {
    assertEquals(null, null);
    assertEquals(1, 1);
    assertEquals('A', 'A');
    assertEquals(1.5f, 1.5f);
    assertEquals(1.5, 1.5);
    assertEquals(new int[]{1, 2}, new int[]{1, 2});
    assertEquals(new float[]{1, 2}, new float[]{1, 2});
    assertEquals(new char[]{1, 2}, new char[]{1, 2});
    assertEquals("Hello", "Hello");

    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, null, "Hello");
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, 1, 2);
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, 1.0f, 2.0f);
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, "ASDF", "BDSFG");
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, new int[]{1}, new int[]{2});
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, new int[]{1,2}, new int[]{2});
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, new int[]{1}, new int[]{1,2});
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, new double[]{1}, new double[]{2});
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, new double[]{1,2}, new double[]{2});
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, new double[]{1}, new double[]{1,2});
    shouldFail((Consumer2Ex<Object, Object>)JUnitUtil::assertEquals, AssertionError.class, new int[]{1}, new double[]{1});
  }
}
