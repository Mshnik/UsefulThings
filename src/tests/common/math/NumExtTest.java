package common.math;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static common.JUnitUtil.*;
import static common.math.NumExt.wrap;

/**
 * @author Mshnik
 */
public class NumExtTest {

  private void testIs5(NumExt n) {
    assertEquals(5, n.intValue());
    assertEquals(5.0f, n.floatValue());
    assertEquals(5.0, n.doubleValue());
    assertEquals(5L, n.longValue());
  }

  @Test
  public void testWrapping() {
    NumExt n = wrap(5);
    assertEquals(5, n.getVal());
    testIs5(n);

    NumExt n2 = wrap(5.0f);
    assertEquals(5.0f, n2.getVal());
    testIs5(n2);

    NumExt n3 = wrap(5.0);
    assertEquals(5.0, n3.getVal());
    testIs5(n3);

    NumExt n4 = wrap(5L);
    assertEquals(5L, n4.getVal());
    testIs5(n4);

    NumExt n5 = wrap((short)5);
    assertEquals((short)5, n5.getVal());
    testIs5(n5);

    NumExt n6 = wrap((byte)5);
    assertEquals((byte)5, n6.getVal());
    testIs5(n6);

  }

  @Test
  public void testArithmetic() {
    NumExt n = wrap(5).add(5);
    assertEquals(10, n.getVal());
    n = n.subtract(5);
    assertEquals(5, n.getVal());
    n = n.divide(2);
    assertEquals(2, n.getVal());
    n = n.multiply(2);
    assertEquals(4, n.getVal());

    NumExt n2 = wrap(5.0f).add(5.0f);
    assertEquals(10.0f, n2.getVal());
    n2 = n2.subtract(5.0f);
    assertEquals(5.0f, n2.getVal());
    n2 = n2.divide(2.0f);
    assertEquals(2.5f, n2.getVal());
    n2 = n2.multiply(2.0f);
    assertEquals(5.0f, n2.getVal());
  }

  @Test
  public void testFunctionAndStream() {
    NumExt n = wrap(5).apply(x -> x.doubleValue()*2.0);
    assertEquals(10.0, n.getVal());

    Stream<Number> s = Stream.of(1,1,1,0.5,0.5);
    Number n3 = s.map(NumExt::wrap).reduce(NumExt.wrap(0.0), NumExt::add).getVal();
    assertEquals(4.0, n3);
  }

  @Test
  public void testNegation() {
    assertEquals(-5, wrap(5).negate().intValue());
    assertEquals(0, wrap(0).negate().intValue());
    assertEquals(5, wrap(5).negate().negate().intValue());
  }

  @Test
  public void testIsZero() {
    assertTrue(wrap(0).isZero());
    assertTrue(wrap(0.0f).isZero());
    assertTrue(wrap(0.0).isZero());
    assertTrue(wrap(0L).isZero());
    assertTrue(wrap((short)0).isZero());
    assertTrue(wrap((byte)0).isZero());

    assertFalse(wrap((short)1).isZero());
    assertFalse(wrap((byte)1).isZero());
    assertFalse(wrap(1).isZero());
    assertFalse(wrap(-1).isZero());
    assertFalse(wrap(1L).isZero());
    assertFalse(wrap(-1L).isZero());
    assertFalse(wrap(0.5f).isZero());
    assertFalse(wrap(-0.5f).isZero());
    assertFalse(wrap(0.5).isZero());
    assertFalse(wrap(-0.5).isZero());
  }

  @Test
  public void testEqualityAndHashcode() {
    assertFalse(wrap(0).equals(null));

    NumExt n = wrap(0);
    assertEquals(n, n);

    List<Number> lst = Arrays.asList((byte)5, (short)5, 5, 5L, 5.0f, 5.0);
    for(Number n2 : lst) {
      assertFalse(wrap(n2).add(n2).equals(wrap(n2)));
      for(Number n3 : lst) {
        assertTrue(wrap(n2).equals(wrap(n3)));
        assertEquals(wrap(n2).hashCode(), wrap(n3).hashCode());
      }
    }
  }

  @Test
  public void testComparison() {
    List<Number> lst = Arrays.asList(18L, (byte)2, 7.0, 12.0, 6.0f, (byte)13, 8, 9L, (short)3, 10.0f, 4, 5L, 11.0);
    List<NumExt> lst2 = lst.stream().map(NumExt::wrap).collect(Collectors.toList());

    Collections.sort(lst2);

    Iterator<NumExt> iter1 = lst2.iterator();
    Iterator<NumExt> iter2 = lst2.iterator();
    iter2.next();

    while(iter2.hasNext()) {
      NumExt n1 = iter1.next();
      NumExt n2 = iter2.next();

      assertEquals(-1, n1.compareTo(n2));
      assertEquals(1, n2.compareTo(n1));
    }

    assertEquals(0, wrap(5).compareTo(wrap(5)));
    assertEquals(0, wrap(5L).compareTo(wrap(5)));
    assertEquals(0, wrap(5.0f).compareTo(wrap(5)));
    assertEquals(0, wrap((byte)5).compareTo(wrap(5)));
    assertEquals(0, wrap((short)5).compareTo(wrap(5)));
  }

}
