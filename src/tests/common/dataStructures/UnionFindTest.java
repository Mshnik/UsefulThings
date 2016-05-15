package common.dataStructures;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

public class UnionFindTest {

  @Test
  public void testConstruction() {
    UnionFind<String> a = new UnionFind<String>();

    a.add("A");
    a.add("B");
    a.add("C");

    HashSet<String> s = new HashSet<>();
    s.add("A");
    s.add("B");
    s.add("C");

    assertEquals(s, a.toElmSet());

    UnionFind<String> a2 = new UnionFind<String>(s);
    assertEquals(s, a2.toElmSet());

    a.add("C");
    assertEquals(s, a.toElmSet());

    a.add("D");
    assertFalse(s.equals(a.toElmSet()));
  }

  @Test
  public void testUnionFind() {
    UnionFind<String> a = new UnionFind<String>();

    a.add("A");
    a.add("B");
    a.add("C");

    assertEquals("A", a.find("A"));
    assertEquals("B", a.find("B"));
    assertEquals("C", a.find("C"));
    assertEquals(1, a.size("A"));
    assertEquals(1, a.size("B"));
    assertEquals(1, a.size("C"));

    assertFalse(a.isUnion("A", "B"));
    assertFalse(a.isUnion("A", "C"));
    assertFalse(a.isUnion("B", "C"));

    String parent = a.union("A", "B");
    String other = (parent.equals("A") ? "B" : "A");
    assertEquals(parent, a.find("A"));
    assertEquals(parent, a.find("B"));
    assertEquals(2, a.size(parent));
    assertEquals(2, a.size(other));
    assertTrue(a.isUnion("A", "B"));
    assertFalse(a.isUnion("A", "C"));
    assertFalse(a.isUnion("B", "C"));

    assertEquals("C", a.find("C"));
    assertEquals(1, a.size("C"));

    parent = a.union("A", "C");
    assertEquals(parent, a.find("A"));
    assertEquals(parent, a.find("B"));
    assertEquals(parent, a.find("C"));

    assertEquals(3, a.size(parent));
    assertEquals(3, a.size("C"));


    assertTrue(a.isUnion("A", "B"));
    assertTrue(a.isUnion("A", "C"));
    assertTrue(a.isUnion("B", "C"));
  }

  @Test
  public void testSize() {
    UnionFind<String> a = new UnionFind<>();
    assertEquals(0, a.size());
    assertTrue(a.isEmpty());

    a.add("A");
    assertEquals(1, a.size());
    assertFalse(a.isEmpty());

    a.add("B");
    assertEquals(2, a.size());

    a.add("A");
    assertEquals(2, a.size());
  }

  @Test
  public void testMaxUnionSize() {
    UnionFind<String> a = new UnionFind<>();
    assertEquals(0, a.maxUnionSize());
    assertTrue(a.isEntirelyConnected());

    a.add("A");
    assertEquals(1, a.maxUnionSize());
    assertTrue(a.isEntirelyConnected());

    a.add("B");
    assertEquals(1, a.maxUnionSize());
    assertFalse(a.isEntirelyConnected());

    a.union("A", "B");
    assertEquals(2, a.maxUnionSize());
    assertTrue(a.isEntirelyConnected());

    a.add("C");
    assertEquals(2, a.maxUnionSize());
    assertFalse(a.isEntirelyConnected());

    a.add("D");
    a.add("E");
    a.union("D","E");
    a.union("C","E");
    assertEquals(3, a.maxUnionSize());
    assertFalse(a.isEntirelyConnected());
    a.union("D", "B");
    assertEquals(5, a.maxUnionSize());
    assertTrue(a.isEntirelyConnected());
  }
}
