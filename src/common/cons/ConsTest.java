package common.cons;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class ConsTest {

	private ConsList<Integer> lst;
	
	@Before
	public void setup(){
		lst = new ConsList<Integer>();
	}
	
	@Test
	public void testConstruction() {
		assertEquals("(" + ConsList.NIL_STRING + ")", lst.toString());
		assertEquals(0, lst.size);
		assertEquals(null, lst.tail);
		assertEquals(null, lst.val);
		
		ConsList<Integer> lst2 = lst.cons(1);
		assertEquals("(1)", lst2.toString());
		assertEquals(1, lst2.size);
		assertEquals(new Integer(1), lst2.val);
		assertEquals(lst, lst2.tail);
		
		ConsList<Integer> lst3 = lst2.cons(2);
		assertEquals("(2,1)", lst3.toString());
		assertEquals(2, lst3.size);
		assertEquals(new Integer(2), lst3.val);
		assertEquals(lst2, lst3.tail);
		
		//Test that lst 2 hasn't changed
		assertEquals("(1)", lst2.toString());
		assertEquals(1, lst2.size);
		assertEquals(new Integer(1), lst2.val);
		assertEquals(lst, lst2.tail);
		
		//Branch to new lst
		ConsList<Integer> lst4 = lst2.cons(3);
		assertEquals("(3,1)", lst4.toString());
		assertEquals(2, lst4.size);
		assertEquals(new Integer(3), lst4.val);
		assertEquals(lst2, lst4.tail);
		
		//Test that lst3 hasn't changed
		assertEquals("(2,1)", lst3.toString());
		assertEquals(2, lst3.size);
		assertEquals(new Integer(2), lst3.val);
		assertEquals(lst2, lst3.tail);
	}
	
	@Test
	public void testEquality(){
		assertTrue(lst.equals(lst));
		
		ConsList<Integer> lst2 = lst.cons(1);
		assertFalse(lst2.equals(lst));
		assertFalse(lst.equals(lst2));
		
		ConsList<Integer> lst2Clone = lst2;
		assertTrue(lst2.equals(lst2Clone));
		assertTrue(lst2Clone.equals(lst2));
		
		ConsList<Integer> lst3 = lst.cons(2);
		assertFalse(lst3.equals(lst2));
		assertFalse(lst2.equals(lst3));
		
		ConsList<Integer> lst4 = lst.cons(1);
		assertTrue(lst4.equals(lst2));
		assertTrue(lst2.equals(lst4));
		
		ConsList<Integer> lst22 = lst2.cons(2);
		ConsList<Integer> lst42 = lst4.cons(2);
		assertTrue(lst22.equals(lst42));
		assertTrue(lst42.equals(lst22));
		
		assertFalse(lst22.equals(lst));
		assertFalse(lst.equals(lst22));
	}
	
	@Test
	public void testListFuncs(){
		lst = lst.cons(5).cons(4).cons(3).cons(2).cons(1);
		assertEquals("(1,2,3,4,5)", lst.toString());
		
		assertFalse(lst.isNil());
		assertFalse(lst.tail.isNil());
		assertFalse(lst.tail.tail.isNil());
		assertFalse(lst.tail.tail.tail.isNil());
		assertFalse(lst.tail.tail.tail.tail.isNil());
		assertTrue(lst.tail.tail.tail.tail.tail.isNil());
		
		assertFalse(lst.isLast());
		assertFalse(lst.tail.isLast());
		assertFalse(lst.tail.tail.isLast());
		assertFalse(lst.tail.tail.tail.isLast());
		assertTrue(lst.tail.tail.tail.tail.isLast());
		assertTrue(lst.tail.tail.tail.tail.tail.isLast());
		
		assertEquals(5, lst.size());
		assertEquals(4, lst.tail.size());
		assertEquals(3, lst.tail.tail.size());
		assertEquals(2, lst.tail.tail.tail.size());
		assertEquals(1, lst.tail.tail.tail.tail.size());
		assertEquals(0, lst.tail.tail.tail.tail.tail.size());
		
		assertFalse(lst.isEmpty());
		assertFalse(lst.tail.isEmpty());
		assertFalse(lst.tail.tail.isEmpty());
		assertFalse(lst.tail.tail.tail.isEmpty());
		assertFalse(lst.tail.tail.tail.tail.isEmpty());
		assertTrue(lst.tail.tail.tail.tail.tail.isEmpty());
		
		assertTrue(lst.contains(1));
		assertTrue(lst.contains(2));
		assertTrue(lst.contains(3));
		assertTrue(lst.contains(4));
		assertTrue(lst.contains(5));
		assertFalse(lst.contains(6));
		assertFalse(lst.contains(null));
		
		assertFalse(lst.tail.contains(1));
		assertTrue(lst.tail.contains(2));
		assertTrue(lst.tail.contains(3));
		assertTrue(lst.tail.contains(4));
		assertTrue(lst.tail.contains(5));
		assertFalse(lst.tail.contains(6));
		assertFalse(lst.tail.contains(null));
		
		assertFalse(lst.tail.tail.tail.tail.tail.contains(null));
		
		ArrayList<Integer> a = new ArrayList<Integer>();
		for(int i = 1; i <= 5; i++){
			a.add(i);
		}
		
		assertTrue(lst.containsAll(a));
		assertFalse(lst.tail.containsAll(a));
		
		a.remove(0);
		assertTrue(lst.containsAll(a));
		assertTrue(lst.tail.containsAll(a));
		assertFalse(lst.tail.tail.containsAll(a));
		
		for(int i = 0; i < 5; i++){
			assertEquals(new Integer(i+1), lst.get(i));
			assertEquals(i, lst.indexOf(new Integer(i+1)));
		}
		
		for(int i = 0; i < 4; i++){
			assertEquals(new Integer(i+2), lst.tail.get(i));
			assertEquals(i, lst.tail.indexOf(new Integer(i+2)));
		}
		
		
	}
	
	@Test
	public void testIteratorsAndArrayConversion(){
		assertEquals(0, lst.toArray().length);
		
		lst = lst.cons(5).cons(4).cons(3).cons(2).cons(1);
		assertEquals("(1,2,3,4,5)", lst.toString());
		Object[] correctArr = {1,2,3,4,5};
		Object[] arr = lst.toArray();
		assertEquals(correctArr.length, arr.length);
		for(int i = 0; i < arr.length; i++){
			assertEquals(correctArr[i], arr[i]);
		}
		Integer[] arr2 = lst.toArray(new Integer[0]);
		assertEquals(correctArr.length, arr2.length);
		for(int i = 0; i < correctArr.length; i++){
			assertEquals(correctArr[i], arr2[i]);
		}
		arr2 = lst.toArray(new Integer[999]);
		assertEquals(999, arr2.length);
		for(int i = 0; i < correctArr.length; i++){
			assertEquals(correctArr[i], arr2[i]);
		}
		
		Iterator<Integer> iterator = lst.iterator();
		for(int i = 1; i <= 5; i++){
			assertTrue(iterator.hasNext());
			assertEquals(new Integer(i), iterator.next());
		}
		assertFalse(iterator.hasNext());
		
		iterator = lst.tail.iterator();
		for(int i = 2; i <= 5; i++){
			assertTrue(iterator.hasNext());
			assertEquals(new Integer(i), iterator.next());
		}
		assertFalse(iterator.hasNext());
		
		iterator = lst.tail.tail.tail.tail.tail.iterator();
		assertFalse(iterator.hasNext());
	}

}
