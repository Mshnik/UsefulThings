package functional;

import static common.JUnitUtil.shouldFail;
import static org.junit.Assert.*;

import functional.impl.*;
import functional.impl.ex.SupplierEx;
import org.junit.Test;

public class FunctionalTest {

  @Test
  public void testCurrying() {
    Function3<Integer, Integer, Integer, Integer> f3 = (a, b, c) -> a + b * c;
    Function2<Integer, Integer, Integer> f3_c1 = f3.partialApply(5);
    Function2<Integer, Integer, Integer> f2 = (a, b) -> 5 + a * b;

    for (int i = -5; i < 5; i++) {
      for (int j = -5; j < 5; j++) {
        assertEquals(f2.apply(i, j), f3_c1.apply(i, j));
      }
    }

    Function1<Integer, Integer> f3_c2 = f3.partialApply(5, 3);
    Function1<Integer, Integer> f2_c1 = f2.partialApply(3);
    Function1<Integer, Integer> f1 = (a) -> 5 + 3 * a;

    for (int i = -5; i < 5; i++) {
      assertEquals(f1.apply(i), f2_c1.apply(i));
      assertEquals(f1.apply(i), f3_c2.apply(i));
    }

    Supplier<Integer> f3_c3 = f3.partialApply(5, 3, 2);
    Supplier<Integer> f2_c2 = f2.partialApply(3, 2);
    Supplier<Integer> f1_c1 = f1.partialApply(2);
    Supplier<Integer> s = () -> 11;

    assertEquals(s.apply(), f3_c3.apply());
    assertEquals(s.apply(), f2_c2.apply());
    assertEquals(s.apply(), f1_c1.apply());
  }

  @Test
  public void testLazyCurrying() {
    Function3<Integer, Integer, Integer, Integer> f3 = (a, b, c) -> a + b * c;
    Function2<Integer, Integer, Integer> f3_c1 = f3.lazyApply(() -> 5);
    Function2<Integer, Integer, Integer> f2 = (a, b) -> 5 + a * b;

    for (int i = -5; i < 5; i++) {
      for (int j = -5; j < 5; j++) {
        assertEquals(f2.apply(i, j), f3_c1.apply(i, j));
      }
    }

    Function1<Integer, Integer> f3_c2 = f3.lazyApply(() -> 5, () -> 3);
    Function1<Integer, Integer> f2_c1 = f2.lazyApply(() -> 3);
    Function1<Integer, Integer> f1 = (a) -> 5 + 3 * a;

    for (int i = -5; i < 5; i++) {
      assertEquals(f1.apply(i), f2_c1.apply(i));
      assertEquals(f1.apply(i), f3_c2.apply(i));
    }

    Supplier<Integer> f3_c3 = f3.lazyApply(() -> 5, () -> 3, () -> 2);
    Supplier<Integer> f2_c2 = f2.lazyApply(() -> 3, () -> 2);
    Supplier<Integer> f1_c1 = f1.lazyApply(() -> 2);
    Supplier<Integer> s = () -> 11;

    assertEquals(s.apply(), f3_c3.apply());
    assertEquals(s.apply(), f2_c2.apply());
    assertEquals(s.apply(), f1_c1.apply());

    //Test when side-effects occur based on partial vs lazy applying
    MutableClass mut = new MutableClass();
    Supplier<MutableClass> getMutAndInc = () -> {
      mut.x++;
      return mut;
    };
    Function1<MutableClass, Integer> getFour = (m) -> 4;

    Supplier<Integer> partialAppFunc1 = getFour.partialApply(getMutAndInc.apply());
    assertEquals(1, mut.x);

    Supplier<Integer> lazyAppFunc1 = getFour.lazyApply(getMutAndInc);
    assertEquals(1, mut.x);

    Supplier<Integer> partialAppFunc2 = getFour.partialApply(getMutAndInc.apply());
    assertEquals(2, mut.x);

    Supplier<Integer> lazyAppFunc2 = getFour.lazyApply(getMutAndInc);
    assertEquals(2, mut.x);

    lazyAppFunc1.apply();
    assertEquals(3, mut.x);

    lazyAppFunc2.apply();
    assertEquals(4, mut.x);

    partialAppFunc1.apply();
    assertEquals(4, mut.x);

    partialAppFunc2.apply();
    assertEquals(4, mut.x);
  }

  private static class MutableClass{
    private int x;
  }

  @Test
  public void testAndThen() {
    Function1<Integer, Integer> f1 = (a) -> (a + 2);
    Function1<Integer, Integer> f2 = (a) -> -a;

    assertEquals(-5, f1.andThen(f2).apply(3).intValue());
    assertEquals(-1, f2.andThen(f1).apply(3).intValue());
    assertEquals(1, f2.andThen(f1).andThen(f1).apply(3).intValue());
  }

  @Test
  public void testPredicateLogic() {
    Predicate1<Integer> p1 = (a) -> a > 5;
    Predicate1<Integer> p2 = (a) -> a < 8;

    assertTrue(p1.apply(8));
    assertFalse(p1.apply(5));
    assertTrue(p2.apply(3));
    assertFalse(p2.apply(9));

    assertTrue(p1.negate().apply(3));
    assertFalse(p1.negate().apply(7));

    assertTrue(p1.negate().negate().apply(7));

    assertTrue(p1.and(p2).apply(6));
    assertFalse(p1.and(p2).apply(4));
    assertFalse(p1.and(p2).apply(9));

    assertTrue(p1.and(p2.negate()).apply(10));
  }

  @Test
  public void testExceptionalInterfaces() {
    SupplierEx<String> s = () -> { throw new Exception(); };

    shouldFail(s, Exception.class);


  }

}
