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

}
