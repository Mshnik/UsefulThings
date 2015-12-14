package common.math;

import functional.impl.ex.Function1Ex;
import org.junit.Test;

import static common.JUnitUtil.*;

public class SequenceAndSumTest {

  @Test
  public void testEmpiricalSequence() {
    Sequence s = new EmpiricalSequence((i) -> i * 2.0);

    for(int i = 0; i < 10; i++) {
      assertEquals(i * 2, (int)s.compute(i));
    }

    assertEquals(50, (int)s.compute(25));

    Function1Ex<Integer, Double> f = s::compute;
    shouldFail(f, IllegalArgumentException.class, -5);
  }

  @Test
  public void testRecursiveSequence() {
    Sequence s = new RecursiveSequence(1.0, (d) -> d * 2.0);

    int x = 1;
    for(int i = 0; i < 10; i++) {
      assertEquals(x, (int)s.compute(i));
      x *= 2;
    }

    assertEquals((int)Math.pow(2.0, 25.0), (int)s.compute(25));
    Function1Ex<Integer, Double> f = s::compute;
    shouldFail(f, IllegalArgumentException.class, -5);
  }

  @Test
  public void testSum() {
    Sequence s = new Sum((i) -> i.doubleValue());
    int x = 0;
    for(int i = 0; i < 10; i++) {
      x += i;
      assertEquals(x, (int)s.compute(i));
    }

    Function1Ex<Integer, Double> f = s::compute;
    shouldFail(f, IllegalArgumentException.class, -5);
  }

  @Test
  public void testSequenceAddition() {
    Sequence s = new EmpiricalSequence((i) -> i * 2.0);
    Sequence s2 = new EmpiricalSequence((i) -> i * 3.0);
    Sequence s3 = s.add(s2);

    for(int i = 0; i < 10; i++) {
      assertEquals(5 * i, (int)s3.compute(i));
    }

    Function1Ex<Integer, Double> f = s::compute;
    shouldFail(f, IllegalArgumentException.class, -5);
  }
}
