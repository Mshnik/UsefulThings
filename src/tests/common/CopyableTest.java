package common;

import org.junit.Test;

import static common.JUnitUtil.*;

/**
 * @author Mshnik
 */
public class CopyableTest {

  @Test
  public void testCreationAndGet() {
    assertEquals(null, Copyable.of(null).get());
    assertEquals(1, Copyable.of(1).get());
    assertEquals(2, Copyable.of(2).get());
    assertEquals("Hello", Copyable.of("Hello").get());
    Object o = new Object();
    assertTrue(o == Copyable.of(o).get());

    assertEquals(null, Copyable.of(null).copy().get());
    assertEquals(1, Copyable.of(1).copy().get());
    assertEquals(2, Copyable.of(2).copy().get());
    assertEquals("Hello", Copyable.of("Hello").copy().get());
    Object o2 = new Object();
    assertTrue(o2 == Copyable.of(o2).copy().get());
  }

  @Test
  public void testEquality() {
    assertFalse(Copyable.of(1).equals(Copyable.of(1)));
    assertFalse(Copyable.of(1).equals(Copyable.of(2)));
    Copyable<Integer> c = Copyable.of(1);
    assertFalse(c.equals(c.copy()));
    assertFalse(c.copy().equals(c));
  }
}
