package common.dataStructures;

import static common.JUnitUtil.shouldFail;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.junit.Test;

import common.types.Tuple2;

public class LinkedHashMapTest {

  @Test
  public void testBasicHashmap() {
    LinkedHashMap<String, Integer> h = new LinkedHashMap<>();

    assertEquals(0, h.size());

    h.put("A", 2);
    assertEquals(1, h.size());
    assertEquals(new Integer(2), h.get("A"));
    assertEquals(null, h.get("B"));

    Integer o = h.put("B", 3);
    assertEquals(2, h.size());
    assertEquals(new Integer(2), h.get("A"));
    assertEquals(new Integer(3), h.get("B"));
    assertEquals(null, o);

    Integer two = h.put("A", 5);
    assertEquals(2, h.size());
    assertEquals(new Integer(5), h.get("A"));
    assertEquals(new Integer(3), h.get("B"));
    assertEquals(new Integer(2), two);

    o = h.remove("D");
    assertEquals(null, o);

    h.clear();
    assertEquals(h.size(), 0);

    //Check rehashing
    for (int i = 65; i < 90; i++) {
      h.put((char) i + "", i);
    }
    for (int i = 65; i < 90; i++) {
      assertEquals(new Integer(i), h.get((char) i + ""));
    }
  }

  @Test
  public void testEqualityAndHashcode() {
    LinkedHashMap<String, Integer> m = new LinkedHashMap<String, Integer>();
    assertEquals(m, m);
    assertFalse(m.equals(null));

    LinkedHashMap<String, Integer> m2 = new LinkedHashMap<String, Integer>();
    assertEquals(m, m2);
    assertEquals(m.hashCode(), m2.hashCode());

    m.put("1", 1);
    assertFalse(m.equals(m2));
    assertFalse(m2.equals(m));

    m2.put("1", 1);
    assertEquals(m, m2);
    assertEquals(m.hashCode(), m2.hashCode());

    m.put("2", 2);
    m2.put("3", 3);
    assertFalse(m.equals(m2));
    assertFalse(m2.equals(m));
  }

  @Test
  public void testIterators() {
    LinkedHashMap<String, Integer> m = new LinkedHashMap<String, Integer>();

    for (int i = 65; i < 90; i++) {
      m.put((char) i + "", i);
    }


    int n = 65;
    Iterator<Entry<String, Integer>> i = m.iterator();
    while (i.hasNext()) {
      Entry<String, Integer> e = i.next();
      assertEquals(e.getValue(), new Integer(n));
      assertEquals(e.getKey(), (char) n + "");
      n++;
    }

    i = m.iterator();
    while (i.hasNext()) {
      i.remove();
      i.next();
    }
    assertEquals(m.size(), 0);
  }

  @Test
  public void testPutAt() {
    LinkedHashMap<String, Integer> m = new LinkedHashMap<String, Integer>();

    assertEquals("{}", m.toString());

    m.put("A", 1);
    assertEquals("{A=1}", m.toString());

    m.put("B", 2);
    assertEquals("{A=1, B=2}", m.toString());

    m.putLast("C", 2);
    assertEquals("{A=1, B=2, C=2}", m.toString());

    m.putFirst("D", 5);
    assertEquals("{D=5, A=1, B=2, C=2}", m.toString());

    m.putAt("E", 4, 1);
    assertEquals("{D=5, E=4, A=1, B=2, C=2}", m.toString());

    m.putAt("F", 6, m.size());
    assertEquals("{D=5, E=4, A=1, B=2, C=2, F=6}", m.toString());

    //Test that a key gets replaced
    m.put("B", 8);
    assertEquals("{D=5, E=4, A=1, B=8, C=2, F=6}", m.toString());

    shouldFail(m::putAt, "E", 5, 2, RuntimeException.class);
  }

  private static class E extends Tuple2<String, Integer> implements Entry<String, Integer> {

    public E(String first, Integer second) {
      super(first, second);
    }

    @Override
    public String getKey() {
      return _1;
    }

    @Override
    public Integer getValue() {
      return _2;
    }

    @Override
    public Integer setValue(Integer value) {
      throw new RuntimeException();
    }
  }

  @Test
  public void testAdd() {
    LinkedHashMap<String, Integer> m = new LinkedHashMap<String, Integer>();

    assertTrue(m.add(new E("1", 1)));
    assertEquals("{1=1}", m.toString());

    assertTrue(m.add(new E("3", 3)));
    assertTrue(m.add(1, new E("2", 2)));
    assertEquals("{1=1, 2=2, 3=3}", m.toString());

    LinkedList<E> ll = new LinkedList<>();
    ll.add(new E("4", 4));
    ll.add(new E("5", 5));

    assertTrue(m.addAll(ll));
    assertEquals("{1=1, 2=2, 3=3, 4=4, 5=5}", m.toString());

    ll.clear();
    ll.add(new E("A", 10));
    ll.add(new E("B", 11));
    assertTrue(m.addAll(2, ll));
    assertEquals("{1=1, 2=2, A=10, B=11, 3=3, 4=4, 5=5}", m.toString());

    assertFalse(m.add(new E("3", 5)));
  }

  @Test
  public void testGetSet() {
    LinkedHashMap<String, Integer> m = new LinkedHashMap<>();

    try {
      m.get(0);
      fail("Got out of bounds index");
    } catch (IllegalArgumentException e) {
    }

    m.put("A", 1);
    Entry<String, Integer> e = m.get(0);
    assertEquals("A", e.getKey());
    assertEquals(new Integer(1), e.getValue());
    assertEquals(e, m.getFirst());
    assertEquals(e, m.getLast());

    m.put("B", 2);
    m.put("C", 3);

    assertEquals("A", m.getFirst().getKey());
    assertEquals("B", m.get(1).getKey());
    assertEquals(new Integer(3), m.getLast().getValue());
    //TODO - set testing

  }

}
