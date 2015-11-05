package common.dataStructures;

import org.junit.Test;

import static common.JUnitUtil.assertEquals;
import static org.junit.Assert.*;

public class BloomFilterTest {

  @Test
  public void testAddAndContains() {
    BloomFilter<Integer> b = new BloomFilter<>();
    assertEquals(0, b.size());

    b.add(5);
    assertEquals(1, b.size());
    assertTrue(b.contains(5));

    b.add(5);
    assertEquals(2, b.size());
    assertTrue(b.contains(5));

    assertFalse(b.contains(4));

    //Check a false positive is possible
    b.add(9);
    b.add(11);
    b.add(20);
    b.add(30);

    assertTrue(b.contains(10));
  }

}
