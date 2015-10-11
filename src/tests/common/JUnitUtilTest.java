package common;

import functional.Consumer;
import functional.FunctionalUtil;
import functional.Unit;
import org.junit.Assert;
import org.junit.Test;

import static functional.FunctionalUtil.*;
import static org.junit.Assert.*;
import static common.JUnitUtil.*;

public class JUnitUtilTest {

  @Test
  public void testTestOn() {
    assertEquals(100, JUnitUtil.testAll(null));
    assertEquals(100, JUnitUtil.testAll());

    assertEquals(0, JUnitUtil.testAll(Assert::fail));
    assertEquals(0, JUnitUtil.testAll(Assert::fail, Assert::fail));

    assertEquals(100, JUnitUtil.testAll(assertEqualsFunc(1,1)));
    assertEquals(100, JUnitUtil.testAll(assertEqualsFunc(1,1), assertEqualsFunc(2,2)));

    assertEquals(50, JUnitUtil.testAll(assertEqualsFunc(1,2), assertEqualsFunc(2,2)));
    assertEquals(66, JUnitUtil.testAll(assertEqualsFunc(1,1), assertEqualsFunc(2,2), assertEqualsFunc(2,3)));
  }

  @Test
  public void testShouldFail() {
    shouldFail(JUnitUtilTest::fails);
    shouldFail(migrate(JUnitUtilTest::runsIfNotZero), 0);
    shouldFail(migrate(JUnitUtilTest::runsIfNeitherZero), 0, 1);
    shouldFail(migrate(JUnitUtilTest::runsIfNeitherZero), 1, 0);

    shouldFail(JUnitUtilTest::fails, RuntimeException.class);
    shouldFail(migrate(JUnitUtilTest::runsIfNotZero), RuntimeException.class, 0);
    shouldFail(migrate(JUnitUtilTest::runsIfNeitherZero), RuntimeException.class, 0, 1);
    shouldFail(migrate(JUnitUtilTest::runsIfNeitherZero), RuntimeException.class, 1, 0);
  }

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
}
