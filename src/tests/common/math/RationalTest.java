package common.math;

import functional.impl.ex.Function2Ex;
import org.junit.Test;

import static common.JUnitUtil.*;

/**
 * @author Mshnik
 */
public class RationalTest {

  @Test
  public void testConstruction() {
    Rational r = Rational.wrap(1,1);
    assertEquals(1.0, r.getVal());
    assertEquals(1, r.getNumerator().intValue());
    assertEquals(1, r.getDenominator().intValue());

    //Check that signs get moved to numerator
    Rational r2 = Rational.wrap(-1,1);
    assertEquals(-1.0, r2.getVal());
    assertEquals(-1, r2.signum());
    assertEquals(-1, r2.getNumerator().intValue());
    assertEquals(1, r2.getDenominator().intValue());

    Rational r3 = Rational.wrap(1,-1);
    assertEquals(-1.0, r3.getVal());
    assertEquals(-1, r3.signum());
    assertEquals(-1, r3.getNumerator().intValue());
    assertEquals(1, r3.getDenominator().intValue());

    Rational r4 = Rational.wrap(-1, -1);
    assertEquals(1, r4.signum());
    assertEquals(1.0, r4.getVal());
    assertEquals(1, r4.getNumerator().intValue());
    assertEquals(1, r4.getDenominator().intValue());

    //Check that it always reduces to lowest terms
    Rational r5 = Rational.wrap(4,2);
    assertEquals(2.0, r5.getVal());
    assertEquals(2, r5.getNumerator().intValue());
    assertEquals(1, r5.getDenominator().intValue());

    Rational r6 = Rational.wrap(40,16);
    assertEquals(2.5, r6.getVal());
    assertEquals(5, r6.getNumerator().intValue());
    assertEquals(2, r6.getDenominator().intValue());

    Rational r7 = Rational.wrap((40 * 1.1), (16 * 1.1));
    assertEquals(2.5, r7.getVal());
    assertEquals(5.0, r7.getNumerator().doubleValue());
    assertEquals(2.0, r7.getDenominator().doubleValue());

    //Check single arg constructor
    assertEquals(5.0, Rational.wrap(5).getVal());

    shouldFail((Function2Ex<Number, Number, Rational>) Rational::wrap, IllegalArgumentException.class, 1, 0);

    //Make sure wrap doesn't nest
    assertTrue(r == Rational.wrap(r));
    assertTrue(r == NumExt.wrap(r));
  }

}
