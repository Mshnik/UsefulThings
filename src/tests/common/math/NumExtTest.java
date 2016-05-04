package common.math;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

import static common.JUnitUtil.*;
import static common.math.NumExt.wrap;

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
    NumExt<Integer> n = wrap(5);
    assertEquals(5, n.asNumber());
    testIs5(n);

    NumExt<Float> n2 = wrap(5.0f);
    assertEquals(5.0f, n2.asNumber());
    testIs5(n2);

    NumExt<Double> n3 = wrap(5.0);
    assertEquals(5.0, n3.asNumber());
    testIs5(n3);

    NumExt<Long> n4 = wrap(5L);
    assertEquals(5L, n4.asNumber());
    testIs5(n4);

    NumExt<Short> n5 = wrap((short)5);
    assertEquals((short)5, n5.asNumber());
    testIs5(n5);

    NumExt<Byte> n6 = wrap((byte)5);
    assertEquals((byte)5, n6.asNumber());
    testIs5(n6);

    NumExt<BigInteger> n7 = wrap(new BigInteger("5"));
    assertEquals(new BigInteger("5"), n7.asNumber());
    testIs5(n7);

    NumExt<BigDecimal> n8 = wrap(new BigDecimal("5.0"));
    assertEquals(new BigDecimal("5.0"), n8.asNumber());
    testIs5(n8);

  }

  @Test
  public void testArithmetic() {
    NumExt<?> n = wrap(5).add(5);
    assertEquals(10, n.asNumber());
    n = n.subtract(5);
    assertEquals(5, n.asNumber());
    n = n.divide(2);
    assertEquals(2, n.asNumber());
    n = n.multiply(2);
    assertEquals(4, n.asNumber());

    NumExt<?> n2 = wrap(5.0f).add(5.0f);
    assertEquals(10.0f, n2.asNumber());
    n2 = n2.subtract(5.0f);
    assertEquals(5.0f, n2.asNumber());
    n2 = n2.divide(2.0f);
    assertEquals(2.5f, n2.asNumber());
    n2 = n2.multiply(2.0f);
    assertEquals(5.0f, n2.asNumber());
  }

  @Test
  public void testFunctionAndStream() {
    NumExt<Double> n = wrap(5).apply((x) -> x*2.0);
    assertEquals(10.0, n.asNumber());

    NumExt<Integer> n2 = wrap(new BigInteger("5")).apply(BigInteger::intValue);
    assertEquals(5, n2.asNumber());

    Stream<Number> s = Stream.of(1,1,1,0.5,0.5);
    //Number n3 = s.map(NumExt::wrap).reduce(NumExt.wrap(0.0), (a, b) -> a.add(b)).get().asNumber();
    //assertEquals(5.0, n3);
  }

}
