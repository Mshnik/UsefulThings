package common.cons;

import static org.junit.Assert.*;

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

}
