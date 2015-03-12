package common.dataStructures;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class DeArrListTest {

	@Test
	public void testAdd() {
		DeArrList<Integer> a = new DeArrList<Integer>(8);
		assertEquals("()", a.toString());
		assertTrue(a.isEmpty());

		a.add(1);
		assertEquals("(1)",a.toString());
		assertEquals(1, a.size());
		assertFalse(a.isEmpty());

		a.add(2);
		assertEquals("(1,2)",a.toString());
		assertEquals(2, a.size());

		a.add(3);
		assertEquals("(1,2,3)",a.toString());
		assertEquals(3, a.size());

		a.add(4);
		assertEquals("(1,2,3,4)",a.toString());
		assertEquals(4, a.size());

		a.add(5);
		assertEquals("(1,2,3,4,5)",a.toString());
		assertEquals(5, a.size());

		a.add(6);
		assertEquals("(1,2,3,4,5,6)",a.toString());
		assertEquals(6, a.size());

		a.add(7);
		assertEquals("(1,2,3,4,5,6,7)",a.toString());
		assertEquals(7, a.size());

		//Test reArraying working correctly
		DeArrList<Integer> a2 = new DeArrList<Integer>(4);
		String s = "(";
		assertTrue(a2.isEmpty());
		for(int i = 0; i < 50; i++){
			a2.add(i);
			assertEquals(i+1, a2.size());
			assertFalse(a2.isEmpty());
			s += i + (i < 49 ? "," : "");
		}
		assertEquals(s + ")", a2.toString());
	}

	@Test
	public void testGet(){
		DeArrList<Integer> a = new DeArrList<>();
		for(int i = 0; i < 50; i++){
			a.add(i);
		}
		for(int i = 0; i < 50; i++){
			assertEquals(new Integer(i), a.get(i));
		}
		
		try{
			a.get(-1);
			fail("Got an out of bounds element");
		}catch(ArrayIndexOutOfBoundsException e){}
		
		try{
			a.get(a.size());
			fail("Got an out of bounds element");
		}catch(ArrayIndexOutOfBoundsException e){}
	}

	@Test
	public void testPrepend(){
		DeArrList<Integer> a = new DeArrList<>();
		assertEquals("()", a.toString());

		a.prepend(5);
		assertEquals("(5)", a.toString());
		assertEquals(1, a.size());

		a.prepend(4);
		assertEquals("(4,5)", a.toString());
		assertEquals(2, a.size());

		a.prepend(3);
		assertEquals("(3,4,5)", a.toString());
		assertEquals(3, a.size());

		a.prepend(2);
		assertEquals("(2,3,4,5)", a.toString());
		assertEquals(4, a.size());

		a.prepend(1);
		assertEquals("(1,2,3,4,5)", a.toString());
		assertEquals(5, a.size());

		a.prepend(0);
		assertEquals("(0,1,2,3,4,5)", a.toString());
		assertEquals(6, a.size());

		a.prepend(-1);
		assertEquals("(-1,0,1,2,3,4,5)", a.toString());
		assertEquals(7, a.size());

		//Test reArraying working correctly
		DeArrList<Integer> a2 = new DeArrList<Integer>(4);
		String s = "(";
		for(int i = 49; i >= 0; i--){
			a2.prepend(i);
		}
		for(int i = 0; i < 50; i++){
			s += i + (i < 49 ? "," : "");
		}
		assertEquals( s + ")", a2.toString());
	}

	@Test
	public void testAddAt(){
		DeArrList<Integer> a = new DeArrList<Integer>(8);
		a.add(1);
		a.add(3);
		a.add(6);

		assertEquals("(1,3,6)",a.toString());
		assertEquals(3, a.size());

		a.add(1, 2);
		assertEquals("(1,2,3,6)",a.toString());
		assertEquals(4, a.size());

		a.add(3, 5);
		assertEquals("(1,2,3,5,6)",a.toString());
		assertEquals(5, a.size());

		a.add(1, 4);
		assertEquals("(1,4,2,3,5,6)",a.toString());
		assertEquals(6, a.size());
		
		a.add(1,12);
		assertEquals("(1,12,4,2,3,5,6)",a.toString());
		assertEquals(7, a.size());
		
		a.add(5,45);
		assertEquals("(1,12,4,2,3,45,5,6)",a.toString());
		assertEquals(8, a.size());
		
		a.add(0,10);
		assertEquals("(10,1,12,4,2,3,45,5,6)",a.toString());
		assertEquals(9, a.size());
		
		a.add(9,99);
		assertEquals("(10,1,12,4,2,3,45,5,6,99)",a.toString());
		assertEquals(10, a.size());
		
		try{
			a.add(12, 3);
			fail("Added at out of bounds position");
		}catch(ArrayIndexOutOfBoundsException e){}

		try{
			a.add(-1, 3);
			fail("Added at out of bounds position");
		}catch(ArrayIndexOutOfBoundsException e){}
		
		//Test reArraying working correctly
		DeArrList<Integer> a2 = new DeArrList<Integer>(4);
		String s = "(";
		a2.add(0);
		a2.add(49);
		for(int i = 1; i < 49; i++){
			a2.add(i, i);
		}
		for(int i = 0; i < 50; i++){
			s += i + (i < 49 ? "," : "");
		}
		assertEquals( s + ")", a2.toString());
	}
	
	@Test
	public void testAddCollections(){
		DeArrList<Integer> a = new DeArrList<Integer>();
		a.add(1);
		a.add(2);
		a.add(3);
		a.add(4);
		
		List<Integer> a2 = a.clone();
		
		assertEquals(4, a.size());
		assertEquals("(1,2,3,4)", a.toString());
		
		a.addAll(a2);
		
		assertEquals(8, a.size());
		assertEquals("(1,2,3,4,1,2,3,4)",a.toString());
		
		a.addAll(2, a2);
		
		assertEquals(12, a.size());
		assertEquals("(1,2,1,2,3,4,3,4,1,2,3,4)",a.toString());
	}
	
	@Test
	public void testSet(){
		DeArrList<Integer> a = new DeArrList<>();
		a.add(1);
		a.add(2);
		a.add(3);
		a.add(4);
		a.add(5);
		
		assertEquals(5, a.size());
		assertEquals("(1,2,3,4,5)",a.toString());
		
		a.set(0, 11);
		assertEquals(5, a.size());
		assertEquals("(11,2,3,4,5)", a.toString());
		
		a.set(2, 31);
		assertEquals(5, a.size());
		assertEquals("(11,2,31,4,5)",a.toString());
		
		a.set(4, 51);
		assertEquals(5,a.size());
		assertEquals("(11,2,31,4,51)",a.toString());
		
		try{
			a.set(-1, 2);
			fail("Set an element oob");
		}catch(ArrayIndexOutOfBoundsException e){}
		
		try{
			a.set(6, 2);
			fail("Set an element oob");
		}catch(ArrayIndexOutOfBoundsException e){}
	}

	@Test
	public void testRemove(){
		DeArrList<String> a = new DeArrList<>();
		for(int i = 0; i < 4; i++){
			a.add("" + i);
		}
		
		assertEquals(4, a.size());
		assertEquals("(0,1,2,3)",a.toString());
		
		boolean b = a.remove("1");
		assertTrue(b);
		assertEquals(3, a.size());
		assertEquals("(0,2,3)", a.toString());
		
		String s = a.remove(0);
		assertEquals("0", s);
		assertEquals(2, a.size());
		assertEquals("(2,3)",a.toString());
		
		b = a.remove("3");
		assertTrue(b);
		assertEquals(1, a.size());
		assertEquals("(2)", a.toString());
		
		s = a.remove(0);
		assertEquals("2",s);
		assertEquals(0, a.size());
		assertEquals("()", a.toString());
	}
}
