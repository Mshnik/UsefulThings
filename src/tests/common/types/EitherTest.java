package common.types;

import static org.junit.Assert.*;

import org.junit.Test;

public class EitherTest {

  @Test
  public void testGetters() {
    Either<String, Integer> l = new Left<>("Hello");
    assertTrue(l.isLeft());
    assertEquals("Hello", l.getVal());
    assertEquals("Hello", l.asLeft());
    assertEquals(null, l.asRight());
    assertEquals(String.class, l.getType());
    assertEquals("Hello".hashCode(), l.hashCode());

    Either<String, Integer> r = new Right<>(5);
    assertFalse(r.isLeft());
    assertEquals(5, r.getVal());
    assertEquals(new Integer(5), r.asRight());
    assertEquals(null, r.asLeft());
    assertEquals(Integer.class, r.getType());
    assertEquals(5, r.hashCode());
  }

  @Test
  public void testEquality() {
    Either<String, Integer> l = new Left<>("Hello");
    Either<String, Integer> l2 = new Left<>("Hi");

    assertTrue(l.equals(l));

    assertFalse(l.equals(l2));
    assertFalse(l2.equals(l));

    Either<String, Integer> r = new Right<>(2);

    assertFalse(l.equals(r));
    assertFalse(r.equals(l));

    Either<String, Object> l3 = new Left<>("Hello");
    Either<String, Integer> l4 = new Left<>("Hello");
    assertTrue(l.equals(l3));
    assertTrue(l.equals(l4));
    assertEquals(l.hashCode(), l3.hashCode());
    assertEquals(l.hashCode(), l4.hashCode());

    Either<Integer, String> r2 = new Right<>("Hello");
    assertFalse(l.equals(r2));
  }

}
