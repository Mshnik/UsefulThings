package common.dataStructures;

import static org.junit.Assert.*;

import org.junit.Test;

public class BiMapTest {

	@Test
	public void testConstruction(){
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
	public void testGetPut(){
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

}
