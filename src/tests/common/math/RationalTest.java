package common.math;

import functional.impl.ex.Function2Ex;
import org.junit.Test;

import static common.JUnitUtil.*;
import static common.math.Rational.*;

/**
 * @author Mshnik
 */
public class RationalTest {

  @Test
  public void testConstruction() {
    Rational r = wrap(1,1);
    assertEquals(1.0, r.getVal());
    assertEquals(1, r.getNumerator().intValue());
    assertEquals(1, r.getDenominator().intValue());

    //Check that signs get moved to numerator
    Rational r2 = wrap(-1,1);
    assertEquals(-1.0, r2.getVal());
    assertEquals(-1, r2.signum());
    assertEquals(-1, r2.getNumerator().intValue());
    assertEquals(1, r2.getDenominator().intValue());

    Rational r3 = wrap(1,-1);
    assertEquals(-1.0, r3.getVal());
    assertEquals(-1, r3.signum());
    assertEquals(-1, r3.getNumerator().intValue());
    assertEquals(1, r3.getDenominator().intValue());

    Rational r4 = wrap(-1, -1);
    assertEquals(1, r4.signum());
    assertEquals(1.0, r4.getVal());
    assertEquals(1, r4.getNumerator().intValue());
    assertEquals(1, r4.getDenominator().intValue());

    //Check that it always reduces to lowest terms
    Rational r5 = wrap(4,2);
    assertEquals(2.0, r5.getVal());
    assertEquals(2, r5.getNumerator().intValue());
    assertEquals(1, r5.getDenominator().intValue());

    Rational r6 = wrap(40,16);
    assertEquals(2.5, r6.getVal());
    assertEquals(5, r6.getNumerator().intValue());
    assertEquals(2, r6.getDenominator().intValue());

    Rational r7 = wrap((40 * 1.1), (16 * 1.1));
    assertEquals(2.5, r7.getVal());
    assertEquals(5.0, r7.getNumerator().doubleValue());
    assertEquals(2.0, r7.getDenominator().doubleValue());

    //Check single arg constructor
    assertEquals(5.0, wrap(5).getVal());

    shouldFail((Function2Ex<Number, Number, Rational>) Rational::wrap, ArithmeticException.class, 1, 0);

    //Make sure wrap doesn't nest
    assertTrue(r == wrap(r));
    assertTrue(r == NumExt.wrap(r));
  }

  @Test
  public void testUseConstants() {
    assertTrue(ZERO == wrap(0,1));
    assertTrue(ZERO == wrap(0,10));
    assertTrue(ZERO == wrap(0,-5.5));
    assertTrue(ZERO == wrap(0.0,2));

    assertTrue(ONE == wrap(1,1));
    assertTrue(ONE == wrap(2,2));
    assertTrue(ONE == wrap(-4,-4));
    assertTrue(ONE == wrap(3.5, 3.5));
    assertTrue(ONE == wrap(6.0, 6));

    assertTrue(NEG_ONE == wrap(-1,1));
    assertTrue(NEG_ONE == wrap(1,-1));
    assertTrue(NEG_ONE == wrap(-3.0, 3));
  }

  @Test
  public void testToString() {
    assertEquals("1/2", wrap(1,2).toString());
    assertEquals("1.0/2.0", wrap(1.0,2).toString());
    assertEquals("1.0/2.0", wrap(1.0,2.0).toString());
    assertEquals("1.0/2.0", wrap(1,2.0).toString());
  }

  @Test
  public void testArithmetic() {
    assertEquals(1.0, wrap(1,2).add(wrap(1,2)).getVal());
    assertEquals(1.0, wrap(1,3).add(wrap(2,3)).getVal());

    assertEquals(0.0, wrap(3,2).add(wrap(-3,2)).getVal());
    assertEquals(0.0, wrap(3,2).subtract(wrap(3,2)).getVal());
    assertEquals(2.5, wrap(5,2).subtract(Rational.ZERO).getVal());

    assertEquals(0.25, wrap(1,2).multiply(wrap(1,2)).getVal());
    assertEquals(1.0, wrap(1,2).divide(wrap(1,2)).getVal());

    assertEquals(2.0, Rational.ONE.add(1).getVal());
    assertEquals(2.5, Rational.ONE.add(1.5f).getVal());
    assertEquals(2.5, Rational.ONE.add(1.5).getVal());

    assertEquals(Rational.ONE, Rational.ONE.invert());
    assertEquals(wrap(1,2), wrap(2,1).invert());
    assertEquals(Rational.NEG_ONE, Rational.NEG_ONE.invert());
    shouldFail(Rational::invert, ArithmeticException.class, Rational.ZERO);
  }

  @Test
  public void testEqualityAndHash() {
    assertTrue(ONE.equals(ONE));
    assertFalse(ONE.equals(null));

    assertEquals(ONE.add(ONE), wrap(2,1));
    assertEquals(ONE.add(ONE).hashCode(), wrap(2,1).hashCode());
    assertEquals(wrap(1,2), wrap(2,4));
    assertEquals(wrap(1,2).hashCode(), wrap(2,4).hashCode());
    assertEquals(wrap(1.0,2.0), wrap(1.5, 3.0));
    assertEquals(wrap(1.0,2.0).hashCode(), wrap(1.5, 3.0).hashCode());

    assertFalse(ONE.equals(NEG_ONE));
    assertFalse(ZERO.equals(ONE));
    assertFalse(ZERO.equals(NEG_ONE));
  }

  @Test
  public void testGetters() {
    assertEquals(-1, NEG_ONE.intValue());
    assertEquals(1, ONE.intValue());
    assertEquals(0, ZERO.intValue());

    assertEquals(0, wrap(1,2).intValue());
    assertEquals(0, wrap(1.0,2.0).intValue());
    assertEquals(1, wrap(4,3).intValue());

    assertEquals(0.0, ZERO.doubleValue());
    assertEquals(1.0, ONE.doubleValue());
    assertEquals(-1.0, NEG_ONE.doubleValue());

    assertEquals(0.5, wrap(1,2).doubleValue());
    assertEquals(1.5, wrap(3,2).doubleValue());
  }

}
