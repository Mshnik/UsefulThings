package common.types;

import functional.impl.ex.UnitEx;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import static common.JUnitUtil.*;

public class EitherTest {

  @Test
  public void testGetters() {
    Either<String, Integer> l = Either.createLeft("Hello");
    assertTrue(l.isLeft());
    assertEquals("Hello", l.getVal());
    assertEquals("Hello", l.asLeft());
    shouldFail((UnitEx) l::asRight, RuntimeException.class);
    assertEquals(String.class, l.getType());
    assertEquals("Hello".hashCode(), l.hashCode());

    Either<String, Integer> r = Either.createRight(5);
    assertFalse(r.isLeft());
    assertEquals(5, r.getVal());
    assertEquals(5, r.asRight());
    shouldFail((UnitEx) r::asLeft, RuntimeException.class);
    assertEquals(Integer.class, r.getType());
    assertEquals(5, r.hashCode());
  }

  @Test
  public void testEquality() {
    Either<String, Integer> l = Either.createLeft("Hello");
    Either<String, Integer> l2 = Either.createLeft("Hi");

    assertTrue(l.equals(l));

    assertFalse(l.equals(l2));
    assertFalse(l2.equals(l));

    Either<String, Integer> r = Either.createRight(2);

    assertFalse(l.equals(r));
    assertFalse(r.equals(l));

    Either<String, Object> l3 = Either.createLeft("Hello");
    Either<String, Integer> l4 = Either.createLeft("Hello");
    assertTrue(l.equals(l3));
    assertTrue(l.equals(l4));
    assertEquals(l.hashCode(), l3.hashCode());
    assertEquals(l.hashCode(), l4.hashCode());

    Either<Integer, String> r2 = Either.createRight("Hello");
    assertFalse(l.equals(r2));

    Either<Integer, String> n = Either.createLeft(null);
    assertTrue(n.equals(n));
    assertFalse(n.equals(l4));
    assertFalse(l4.equals(n));
    assertFalse(n.equals(r2));
    assertFalse(r2.equals(n));

    Either<Double, Integer> r3 = Either.createRight(2);
    assertTrue(r3.equals(r));
  }

  @Test
  public void testFiltering() {
    List<Either<String, Integer>> lst = Arrays.asList(Either.createLeft("Hello"), Either.createLeft("Hi"), Either.createRight(1));
    Tuple2<List<String>, List<Integer>> t = Either.filterAndSplit(lst);
    assertEquals(2, t._1.size());
    assertEquals("Hello", t._1.get(0));
    assertEquals("Hi", t._1.get(1));
    assertEquals(1, t._2.size());
    assertEquals(1, t._2.get(0));

    List<String> lst2 = Either.filterA(lst);
    assertEquals(2, lst2.size());
    assertEquals("Hello", lst2.get(0));
    assertEquals("Hi", lst2.get(1));

    List<Integer> lst3 = Either.filterB(lst);
    assertEquals(1, lst3.size());
    assertEquals(1, lst3.get(0));
  }

  @Test
  public void testMapAndReduce() {
    Either<String, Character> e = Either.createLeft("Hello");
    Either<Integer, Integer> e2 = e.map(String::length, Character::getNumericValue);
    Either<Integer, Character> e22 = e.mapLeft(String::length);
    assertEquals(5, e2.asLeft());
    assertEquals(5, e22.asLeft());
    assertEquals(5, e.reduce(String::length, Character::getNumericValue));

    Either<Character, String> e3 = Either.createRight("Hello");
    Either<Integer, Integer> e4 = e3.map(Character::getNumericValue, String::length);
    Either<Character, Integer> e44 = e3.mapRight(String::length);
    assertEquals(5, e4.asRight());
    assertEquals(5, e44.asRight());
    assertEquals(5, e3.reduce(Character::getNumericValue, String::length));
  }

}
