package common.math;

import org.junit.Test;

import static common.JUnitUtil.*;

/**
 * @author Mshnik
 */
public class NumExtTest {

  @Test
  public void testWrapping() {
    NumExt<Integer> n = NumExt.wrap(5);
    assertEquals(5, n.asNumber());
    assertEquals(5, n.asInt());
    assertEquals(5.0f, n.asFloat());
    assertEquals(5.0, n.asDouble());

    NumExt<Float> n2 = NumExt.wrap(5.0f);
    assertEquals(5.0f, n2.asNumber());
    assertEquals(5, n2.asInt());
    assertEquals(5.0f, n2.asFloat());
    assertEquals(5.0, n2.asDouble());

    NumExt<Double> n3 = NumExt.wrap(5.0);
    assertEquals(5.0, n3.asNumber());
    assertEquals(5, n3.asInt());
    assertEquals(5.0f, n3.asFloat());
    assertEquals(5.0, n3.asDouble());
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
