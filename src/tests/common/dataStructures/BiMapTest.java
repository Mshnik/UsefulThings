package common.dataStructures;

import static common.JUnitUtil.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.Test;

public class BiMapTest {

  @Test
  public void testConstruction() {
    BiMap<Character, Integer> b = new BiMap<>();

    assertEquals(0, b.size());

    b.put('a', 0);

    assertTrue(b.containsKey('a'));
    assertFalse(b.containsKey('b'));
    assertTrue(b.containsValue(0));
    assertFalse(b.containsValue(1));
    assertFalse(b.containsKey(0));
    assertFalse(b.containsValue('a'));

    assertEquals(1, b.size());


    b.put('b', 1);

    assertTrue(b.containsKey('a'));
    assertTrue(b.containsKey('b'));
    assertTrue(b.containsValue(0));
    assertTrue(b.containsValue(1));
    assertFalse(b.containsKey(0));
    assertFalse(b.containsValue('a'));

    assertEquals(2, b.size());


    //Try overwriting a key

    b.put('a', 2);
    assertTrue(b.containsKey('a'));
    assertTrue(b.containsKey('b'));
    assertFalse(b.containsValue(0));
    assertTrue(b.containsValue(1));
    assertTrue(b.containsValue(2));
    assertFalse(b.containsKey(0));
    assertFalse(b.containsValue('a'));

    assertEquals(2, b.size());


    //Try overwriting a value

    b.put('c', 1);
    assertTrue(b.containsKey('a'));
    assertFalse(b.containsKey('b'));
    assertTrue(b.containsKey('c'));
    assertFalse(b.containsValue(0));
    assertTrue(b.containsValue(1));
    assertTrue(b.containsValue(2));
    assertFalse(b.containsKey(0));
    assertFalse(b.containsValue('a'));

    assertEquals(2, b.size());
  }

  @Test
  public void testGetPut() {
    BiMap<Character, Integer> b = new BiMap<>();

    assertEquals(null, b.get('a'));
    assertEquals(null, b.getValue('a'));
    assertEquals(null, b.getKey(1));

    Integer old = b.put('a', 1);
    assertEquals(null, old);
    assertEquals(new Integer(1), b.get('a'));
    assertEquals(new Integer(1), b.getValue('a'));
    assertEquals(new Character('a'), b.getKey(1));

    old = b.put('b', 1);
    assertEquals(null, old);
    assertEquals(null, b.get('a'));
    assertEquals(null, b.getValue('a'));
    assertEquals(new Integer(1), b.get('b'));
    assertEquals(new Integer(1), b.getValue('b'));
    assertEquals(new Character('b'), b.getKey(1));

    old = b.remove('b');
    assertEquals(new Integer(1), old);
    assertEquals(0, b.size());
    assertEquals(null, b.get('a'));
    assertEquals(null, b.getValue('a'));
    assertEquals(null, b.get('b'));
    assertEquals(null, b.getValue('b'));
    assertEquals(null, b.getKey(1));
  }

  @Test
  public void testEqualityHashcode() {
    BiMap<String, Integer> b = new BiMap<>();
    assertEquals(b, b);
    assertEquals(b.hashCode(), b.hashCode());
    assertFalse(b.equals(null));

    BiMap<String, Integer> b2 = new BiMap<>();
    assertEquals(b, b2);
    assertEquals(b.hashCode(), b2.hashCode());

    b.put("1", 1);

    assertFalse(b.equals(b2));
    assertFalse(b2.equals(b));

    b2.put("2", 1);

    assertFalse(b.equals(b2));
    assertFalse(b2.equals(b));

    b.put("2", 1);
    assertEquals(b, b2);
    assertEquals(b.hashCode(), b2.hashCode());
  }

  @Test
  public void testViews() {

    //Keys view
    BiMap<String, Integer> b = new BiMap<>();
    HashSet<String> keys = new HashSet<String>();
    HashSet<Integer> vals = new HashSet<>();


    b.put("A", 2);
    b.put("B", 3);
    b.put("C", 4);

    keys.add("A");
    keys.add("B");
    keys.add("C");

    assertEquals(b.keySet().size(), keys.size());
    assertEquals(b.keySet(), keys);

    b.keySet().remove("A");
    keys.remove("A");

    assertEquals(b.keySet().size(), keys.size());
    assertEquals(b.keySet(), keys);

    shouldFail(b.keySet()::add, "F", UnsupportedOperationException.class);

    b.keySet().clear();
    keys.clear();

    assertEquals(b.keySet().size(), keys.size());
    assertEquals(b.keySet(), keys);

    b.put("A", 1);
    b.put("B", 3);
    b.put("C", 4);
    b.put("D", 5);
    b.put("E", 7);

    Iterator<String> keyIterator = b.keySet().iterator();
    while (keyIterator.hasNext()) {
      String s = keyIterator.next();
      if (s.equals("B")) {
        keyIterator.remove();
      }
      if (s.equals("C")) {
        keyIterator.remove();
        keyIterator.remove(); //Make sure double removal doesn't hurt
      }
    }

    assertEquals(b.size(), b.keySet().size());
    assertEquals(3, b.keySet().size());

    BiMap<String, Integer> b2 = new BiMap<>();
    b2.put("A", 1);
    b2.put("D", 5);
    b2.put("E", 7);

    assertEquals(b, b2);

    //Test vals
    b.clear();
    keys.clear();
    vals.clear();

    b.put("A", 2);
    b.put("B", 3);
    b.put("C", 4);

    vals.add(2);
    vals.add(3);
    vals.add(4);

    assertEquals(b.values().size(), vals.size());
    assertEquals(b.values(), vals);

    b.values().remove(2);
    vals.remove(new Integer(2));

    assertEquals(b.values().size(), vals.size());
    assertEquals(b.values(), vals);

    shouldFail(b.values()::add, 7, UnsupportedOperationException.class);

    b.values().clear();
    vals.clear();

    assertEquals(b.values().size(), vals.size());
    assertEquals(b.values(), vals);

    b.put("A", 1);
    b.put("B", 3);
    b.put("C", 4);
    b.put("D", 5);
    b.put("E", 7);

    Iterator<Integer> valuesIterator = b.values().iterator();
    while (valuesIterator.hasNext()) {
      Integer i = valuesIterator.next();
      if (i.equals(3)) {
        valuesIterator.remove();
      }
      if (i.equals(4)) {
        valuesIterator.remove();
        valuesIterator.remove(); //Make sure double removal doesn't hurt
      }
    }

    vals.add(1);
    vals.add(5);
    vals.add(7);

    b2.put("A", 1);
    b2.put("D", 5);
    b2.put("E", 7);

    assertEquals(b.values().size(), vals.size());
    assertEquals(b.values(), vals);

    assertEquals(b, b2);

    //Entry test
    b.clear();
    keys.clear();
    vals.clear();
    b.put("A", 2);
    b.put("B", 3);
    b.put("C", 4);

    keys.add("A");
    keys.add("B");
    keys.add("C");
    vals.add(2);
    vals.add(3);
    vals.add(4);

    assertEquals(b.entrySet().size(), keys.size());
    assertEquals(b.entrySetFlipped().size(), vals.size());

    for (Entry<String, Integer> e : b.entrySet()) {
      assertTrue(keys.contains(e.getKey()));
      assertTrue(vals.contains(e.getValue()));
    }
    for (Entry<Integer, String> e : b.entrySetFlipped()) {
      assertTrue(keys.contains(e.getValue()));
      assertTrue(vals.contains(e.getKey()));
    }

    b.entrySet().clear();
    keys.clear();

    assertEquals(b.entrySet().size(), keys.size());
    assertEquals(b.entrySet().size(), b.entrySetFlipped().size());

    b.put("A", 1);
    b.entrySetFlipped().clear();
    vals.clear();

    assertEquals(b.entrySetFlipped().size(), vals.size());
    assertEquals(b.entrySet().size(), b.entrySetFlipped().size());

    b.put("A", 1);
    b.put("B", 3);
    b.put("C", 4);
    b.put("D", 5);
    b.put("E", 7);

    Iterator<Entry<String, Integer>> entriesIterator = b.entrySet().iterator();
    while (entriesIterator.hasNext()) {
      Entry<String, Integer> e = entriesIterator.next();
      if (e.getKey().equals("B")) {
        entriesIterator.remove();
      }
      if (e.getKey().equals("C")) {
        entriesIterator.remove();
        entriesIterator.remove(); //Make sure double removal doesn't hurt
      }

      shouldFail(e::setValue, 999, UnsupportedOperationException.class);
    }

    assertEquals(b.size(), b.keySet().size());
    assertEquals(3, b.keySet().size());

    b2.clear();
    b2.put("A", 1);
    b2.put("D", 5);
    b2.put("E", 7);

    assertEquals(b, b2);

    Iterator<Entry<Integer, String>> rentriesIterator = b.entrySetFlipped().iterator();
    while (rentriesIterator.hasNext()) {
      Entry<Integer, String> e = rentriesIterator.next();
      if (e.getKey().equals(1)) {
        rentriesIterator.remove();
      }
      if (e.getKey().equals(5)) {
        rentriesIterator.remove();
        rentriesIterator.remove(); //Make sure double removal doesn't hurt
      }

      shouldFail(e::setValue, "VAL", UnsupportedOperationException.class);
    }

    b2.remove("A");
    b2.remove("D");

    assertEquals(b, b2);
  }
}
