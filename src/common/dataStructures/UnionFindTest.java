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
		
		assertEquals(s, a.toSet());
		
		UnionFind<String> a2 = new UnionFind<String>(s);
		assertEquals(s, a2.toSet());
		
		a.add("C");
		assertEquals(s, a.toSet());
		
		a.add("D");
		assertFalse(s.equals(a.toSet()));
	}
	
	@Test
	public void testUnionFind(){
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
		
		String parent = a.union("A", "B");
		String other = (parent.equals("A") ? "B" : "A");
		assertEquals(parent, a.find("A"));
		assertEquals(parent, a.find("B"));
		assertEquals(2, a.size(parent));
		assertEquals(1, a.size(other));
		
		assertEquals("C", a.find("C"));
		assertEquals(1, a.size("C"));
		
		parent = a.union("A", "C");
		assertEquals(parent, a.find("A"));
		assertEquals(parent, a.find("B"));
		assertEquals(parent, a.find("C"));
		
		assertEquals(3, a.size(parent));
		assertEquals(1, a.size("C"));
	}

}
