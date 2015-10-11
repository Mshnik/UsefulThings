package common;

import org.junit.Assert;
import org.junit.Test;

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



  private static void fails() throws RuntimeException {
    throw new RuntimeException();
  }

  private static void runs(){}
}
