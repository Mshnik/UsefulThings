package common.math;

import org.junit.Test;

import static common.JUnitUtil.*;

public class SequenceAndSumTest {

  @Test
  public void testEmpiricalSequence() {
    Sequence s = new EmpiricalSequence((i) -> i * 2.0);

    for(int i = 0; i < 10; i++) {
      assertEquals(i * 2, s.compute(i).intValue());
    }

    assertEquals(50, s.compute(25).intValue());

    shouldFail(s::compute, IllegalArgumentException.class, -5);
  }

  @Test
  public void testRecursiveSequence() {
    Sequence s = new RecursiveSequence(1.0, (d) -> d * 2.0);

    int x = 1;
    for(int i = 0; i < 10; i++) {
      assertEquals(x, s.compute(i).intValue());
      x *= 2;
    }

    assertEquals((int)Math.pow(2.0, 25.0), s.compute(25).intValue());
    shouldFail(s::compute, IllegalArgumentException.class, -5);
  }

  @Test
  public void testSum() {
    Sequence s = new Sum((i) -> i.doubleValue());
    int x = 0;
    for(int i = 0; i < 10; i++) {
      x += i;
      assertEquals(x, s.compute(i).intValue());
    }

    shouldFail(s::compute, IllegalArgumentException.class, -5);
  }

  @Test
  public void testSequenceAddition() {
    Sequence s = new EmpiricalSequence((i) -> i * 2.0);
    Sequence s2 = new EmpiricalSequence((i) -> i * 3.0);
    Sequence s3 = s.add(s2);

    for(int i = 0; i < 10; i++) {
      assertEquals(5 * i, s3.compute(i).intValue());
    }

    shouldFail(s3::compute, IllegalArgumentException.class, -5);
  }
}
