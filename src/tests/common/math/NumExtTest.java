package common.math;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static common.JUnitUtil.*;

/**
 * @author Mshnik
 */
public class NumExtTest {

  private void testIs5(NumExt<?> n) {
    assertEquals(5, n.asInt());
    assertEquals(5.0f, n.asFloat());
    assertEquals(5.0, n.asDouble());
    assertEquals(5L, n.asLong());
  }

  @Test
  public void testWrapping() {
    NumExt<Integer> n = NumExt.wrap(5);
    assertEquals(5, n.asNumber());
    testIs5(n);

    NumExt<Float> n2 = NumExt.wrap(5.0f);
    assertEquals(5.0f, n2.asNumber());
    testIs5(n2);

    NumExt<Double> n3 = NumExt.wrap(5.0);
    assertEquals(5.0, n3.asNumber());
    testIs5(n3);

    NumExt<Long> n4 = NumExt.wrap(5L);
    assertEquals(5L, n4.asNumber());
    testIs5(n4);

    NumExt<Short> n5 = NumExt.wrap((short)5);
    assertEquals((short)5, n5.asNumber());
    testIs5(n5);

    NumExt<Byte> n6 = NumExt.wrap((byte)5);
    assertEquals((byte)5, n6.asNumber());
    testIs5(n6);

    NumExt<BigInteger> n7 = NumExt.wrap(new BigInteger("5"));
    assertEquals(new BigInteger("5"), n7.asNumber());
    testIs5(n7);

    NumExt<BigDecimal> n8 = NumExt.wrap(new BigDecimal("5.0"));
    assertEquals(new BigDecimal("5.0"), n8.asNumber());
    testIs5(n8);

  }

  @Test
  public void testArithmetic() {
    NumExt<Integer> n = NumExt.wrap(5).add(5);
    assertEquals(10, n.asNumber());
    n = n.subtract(5);
    assertEquals(5, n.asNumber());
    n = n.divide(2);
    assertEquals(2, n.asNumber());
    n = n.multiply(2);
    assertEquals(4, n.asNumber());

    NumExt<Float> n2 = NumExt.wrap(5.0f).add(5.0f);
    assertEquals(10.0f, n2.asNumber());
    n2 = n2.subtract(5.0f);
    assertEquals(5.0f, n2.asNumber());
    n2 = n2.divide(2.0f);
    assertEquals(2.5f, n2.asNumber());
    n2 = n2.multiply(2.0f);
    assertEquals(5.0f, n2.asNumber());
  }

}
