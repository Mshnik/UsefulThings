package common;

import functional.impl.ex.Function1Ex;
import functional.impl.ex.Function2Ex;
import functional.impl.ex.SupplierEx;
import org.junit.Test;
import static common.JUnitUtil.*;

public class MethodRunnerTest {


  @Test
  public void testConstruction() {
    Function1Ex<SupplierEx<Object>, MethodRunner<Object>> c = MethodRunner::new;
    Function2Ex<SupplierEx<Object>, Long, MethodRunner<Object>> c2 = MethodRunner::new;
    shouldFail(c, null);
    shouldFail(c2, null, -5L);
    shouldFail(c2, null, 100L);
    shouldFail(c2, () -> null, -5L);

    try{
      new MethodRunner<>(() -> "");
      new MethodRunner<>(() -> null, 5);
    }catch(IllegalArgumentException e) {
      fail("Valid construction failed");
    }
  }

  @Test
  public void testValidRunning() {
    MethodRunner<Integer> mR = new MethodRunner<Integer>(() -> 10);

    assertEquals(new Integer(10), mR.get().asRight());
    try {
      assertEquals(new Integer(10), mR.getOrThrow());
    }catch(Throwable t) {
      fail("Valid call threw throwable " + t);
    }
    assertTrue(mR.getCompletionMillis() < MethodRunner.DEFAULT_WAIT_TIME);

    MethodRunner<Integer> mR2 = new MethodRunner<>(() -> 15);

    assertEquals(new Integer(15), mR2.get().asRight());
    try {
      assertEquals(new Integer(15), mR2.getOrThrow());
    }catch(Throwable t) {
      fail("Valid call threw throwable " + t);
    }

    assertTrue(mR2.getCompletionMillis() < MethodRunner.DEFAULT_WAIT_TIME);
  }

  private static Integer throwsException() throws Throwable {
    throw new RuntimeException("Message here");
  }

  @Test
  public void testExceptionThrown() {
    MethodRunner<Integer> mR = new MethodRunner<>(MethodRunnerTest::throwsException);
    assertEquals("Message here", mR.get().asLeft().getMessage());

    try {
      mR.getOrThrow();
      fail ("Throwing call didn't throw exception");
    } catch(Throwable t) {
      //Good
    }
    assertTrue(mR.getCompletionMillis() < MethodRunner.DEFAULT_WAIT_TIME);


    MethodRunner<Integer> mR2 = new MethodRunner<>(MethodRunnerTest::throwsException);

    try {
      mR2.getOrThrow();
      fail ("Throwing call didn't throw exception");
    } catch(Throwable t) {
      //Good
    }
    assertEquals("Message here", mR2.get().asLeft().getMessage());
    assertTrue(mR2.getCompletionMillis() < MethodRunner.DEFAULT_WAIT_TIME);

  }

  private static Integer stackOverflows(boolean ok) {
    if (ok) return 5;
    else return stackOverflows(false);
  }

  @Test
  public void testStackOverflow() {
    MethodRunner<Integer> mR = new MethodRunner<>(MethodRunnerTest::stackOverflows, false);
    assertEquals(StackOverflowError.class, mR.get().asLeft().getClass());

    try {
      mR.getOrThrow();
      fail ("Stack overflowing call didn't throw exception");
    } catch(Throwable t) {
      //Good
    }
    assertTrue(mR.getCompletionMillis() < MethodRunner.DEFAULT_WAIT_TIME);

    MethodRunner<Integer> mR2 = new MethodRunner<>(MethodRunnerTest::stackOverflows, false);

    try {
      mR2.getOrThrow();
      fail ("Stack overflowing call didn't throw exception");
    } catch(Throwable t) {
      //Good
    }
    assertEquals(StackOverflowError.class, mR2.get().asLeft().getClass());
    assertTrue(mR2.getCompletionMillis() < MethodRunner.DEFAULT_WAIT_TIME);
  }

  private static Integer loopForever() {
    while(true){
      //asdf
    }
  }

  @Test
  public void testInfiniteLoop() {
    final long WAIT_TIME = 500L;
    MethodRunner<Integer> mR = new MethodRunner<>(MethodRunnerTest::loopForever, WAIT_TIME);
    assertEquals(null, mR.get());
    try {
      assertEquals(null, mR.getOrThrow());
    }catch(Throwable t) {
      fail("infinite loop call threw throwable " + t);
    }
    assertTrue(mR.getCompletionMillis() >= WAIT_TIME);

    MethodRunner<Integer> mR2 = new MethodRunner<>(MethodRunnerTest::loopForever, WAIT_TIME);
    try {
      assertEquals(null, mR2.getOrThrow());
    }catch(Throwable t) {
      fail("infinite loop call threw throwable " + t);
    }
    assertEquals(null, mR2.get());
    assertTrue(mR2.getCompletionMillis() >= WAIT_TIME);
  }
}
