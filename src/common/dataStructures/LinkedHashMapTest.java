package common.dataStructures;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map.Entry;

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

	@Test
	public void testIterators(){
		LinkedHashMap<String, Integer> m = new LinkedHashMap<String, Integer>();

		for(int i = 65; i < 90; i++){
			m.put( (char)i + "", i);
		}
		
		
		int n = 65;
		Iterator<Entry<String,Integer>> i = m.iterator();
		while(i.hasNext()){
			Entry<String,Integer> e = i.next();
			assertEquals(e.getValue(), new Integer(n));
			assertEquals(e.getKey(), (char)n + "");
			n++;
		}
		
		i = m.iterator();
		while(i.hasNext()){
			i.remove();
			i.next();
		}
		assertEquals(m.size(), 0);
	}
	
	@Test
	public void testPutAt(){
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

	}
	
}
