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

  private void testIs5(NumExt n) {
    assertEquals(5, n.asInt());
    assertEquals(5.0f, n.asFloat());
    assertEquals(5.0, n.asDouble());
    assertEquals(5L, n.asLong());
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

}
