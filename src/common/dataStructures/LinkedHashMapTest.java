package common.dataStructures;

import static org.junit.Assert.*;

import org.junit.Test;

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
		for(int i = 65; i < 90; i++){
			h.put( (char)i + "", i);
		}
		for(int i = 65; i < 90; i++){
			assertEquals(new Integer(i), h.get((char)i + ""));
		}	
	}
	
	@Test
	public void testEqualityAndHashcode(){
		LinkedHashMap<String, Integer> m = new LinkedHashMap<String, Integer>();
		assertEquals(m,m);
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

}
