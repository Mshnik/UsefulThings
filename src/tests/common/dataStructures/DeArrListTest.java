package common.dataStructures;

import static common.JUnitUtil.*;
import static functional.FunctionalUtil.migrate;
import static org.junit.Assert.*;

import java.util.List;

import functional.BiConsumer;
import functional.Supplier;
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

		a.push(5);
		assertEquals("(5)", a.toString());
		assertEquals(1, a.size());

		a.push(4);
		assertEquals("(4,5)", a.toString());
		assertEquals(2, a.size());

		a.push(3);
		assertEquals("(3,4,5)", a.toString());
		assertEquals(3, a.size());

		a.push(2);
		assertEquals("(2,3,4,5)", a.toString());
		assertEquals(4, a.size());

		a.push(1);
		assertEquals("(1,2,3,4,5)", a.toString());
		assertEquals(5, a.size());

		a.push(0);
		assertEquals("(0,1,2,3,4,5)", a.toString());
		assertEquals(6, a.size());

		a.push(-1);
		assertEquals("(-1,0,1,2,3,4,5)", a.toString());
		assertEquals(7, a.size());

		//Test reArraying working correctly
		DeArrList<Integer> a2 = new DeArrList<Integer>(4);
		String s = "(";
		for(int i = 49; i >= 0; i--){
			a2.push(i);
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
		
		shouldFail( (BiConsumer<Integer, Integer>) a::add, ArrayIndexOutOfBoundsException.class, 12, 3);
		shouldFail( (BiConsumer<Integer, Integer>) a::add, ArrayIndexOutOfBoundsException.class, -1, 3);
		
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
	public void testEqualityAndHashcode(){
		DeArrList<Integer> a = new DeArrList<>();
		DeArrList<Integer> a2 = a.clone();
		
		assertTrue(a.equals(a2));
		assertTrue(a2.equals(a));
		assertFalse(a.equals(null));
		assertEquals(a.hashCode(), a2.hashCode());
		
		a.add(1);
		
		assertFalse(a.equals(a2));
		assertFalse(a2.equals(a));
		assertFalse(a.equals(null));
		assertFalse(a2.equals(null));
		
		a2.add(1);
		assertTrue(a.equals(a2));
		assertTrue(a2.equals(a));
		assertEquals(a.hashCode(), a2.hashCode());
		
		a.add(2);
		a2.add(3);
		
		assertFalse(a.equals(a2));
		assertFalse(a2.equals(a));
		assertFalse(a.equals(null));
		assertFalse(a2.equals(null));
		
		a.removeLast();
		a2.removeLast();
		
		a.add(3);
		a2.add(3);
		
		a.rotateTo(2);
		assertTrue(a.equals(a2));
		assertTrue(a2.equals(a));
		assertEquals(a.hashCode(), a2.hashCode());
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
		
		shouldFail(a::set, ArrayIndexOutOfBoundsException.class, -1, 2);
		shouldFail(a::set, ArrayIndexOutOfBoundsException.class, 6, 2);
	}

	@Test
	public void testRotate(){
		
		//Test rotating a non-full list
		DeArrList<String> a = new DeArrList<>();
		for(int i = 0; i < 4; i++){
			a.add("" + i);
		}
		
		assertEquals(4, a.size());
		assertEquals("(0,1,2,3)",a.toString());
		
		for(int i = 0; i < a.getArrLength(); i++){
			a.rotateTo(i);
			assertEquals(4, a.size());
			assertEquals("(0,1,2,3)",a.toString());
		}
		
		//Test rotating a full list
		a = new DeArrList<>(4);
		for(int i = 0; i < 4; i++){
			a.add("" + i);
		}
		
		assertEquals(4, a.size());
		assertEquals("(0,1,2,3)",a.toString());
		
		for(int i = 0; i < a.getArrLength(); i++){
			a.rotateTo(i);
			assertEquals(4, a.size());
			assertEquals("(0,1,2,3)",a.toString());
		}
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
		
		String s = a.remove(1);
		assertEquals("2", s);
		assertEquals(2, a.size());
		assertEquals("(0,3)",a.toString());
		
		b = a.remove("3");
		assertTrue(b);
		assertEquals(1, a.size());
		assertEquals("(0)", a.toString());
		
		s = a.remove(0);
		assertEquals("0",s);
		assertEquals(0, a.size());
		assertEquals("()", a.toString());
		
		a = new DeArrList<>(4);
		a.add("0");
		a.add("1");
		a.add("2");
		a.add("3");
		
		assertEquals(4, a.size());
		assertEquals("(0,1,2,3)",a.toString());
		
		//Try all rotations and all removals
		for(int i = 0; i < 4; i++){
			a.rotateTo(i);
			
			for(int k = 0; k < 4; k++){
				String elm = a.remove(k);
				assertEquals(3, a.size());
				assertEquals("" + k, elm);
				a.add(k, elm);
			}
		}
		
	}
	
	@Test
	public void testDequeFunctions(){
		DeArrList<Integer> a = new DeArrList<>();
		for(int i = 0; i < 5; i++){
			a.add(i);
		}
		
		assertEquals("(0,1,2,3,4)", a.toString());
		
		a.offer(5);
		assertEquals("(0,1,2,3,4,5)", a.toString());

		a.offerFirst(-1);
		assertEquals("(-1,0,1,2,3,4,5)", a.toString());
		
		a.offerLast(6);
		assertEquals("(-1,0,1,2,3,4,5,6)", a.toString());
		
		a.push(-2);
		assertEquals("(-2,-1,0,1,2,3,4,5,6)", a.toString());

		assertEquals(new Integer(-2), a.peek());
		assertEquals(new Integer(-2), a.peekFirst());
		assertEquals(new Integer(-2), a.element());
		assertEquals(new Integer(6), a.peekLast());

		Integer i = a.poll();
		assertEquals(new Integer(-2), i);
		assertEquals("(-1,0,1,2,3,4,5,6)", a.toString());
		
		i = a.pollFirst();
		assertEquals(new Integer(-1), i);
		assertEquals("(0,1,2,3,4,5,6)", a.toString());
		
		i = a.pollLast();
		assertEquals(new Integer(6), i);
		assertEquals("(0,1,2,3,4,5)", a.toString());
		
		i = a.pop();
		assertEquals(new Integer(0), i);
		assertEquals("(1,2,3,4,5)",a.toString());
		
		a.addFirst(0);
		assertEquals("(0,1,2,3,4,5)", a.toString());

		a.addLast(6);
		assertEquals("(0,1,2,3,4,5,6)", a.toString());

		assertEquals(new Integer(0), a.getFirst());
		assertEquals(new Integer(6), a.getLast());
		
		i = a.removeFirst();
		assertEquals(new Integer(0), i);
		assertEquals("(1,2,3,4,5,6)",a.toString());
		
		i = a.removeLast();
		assertEquals(new Integer(6), i);
		assertEquals("(1,2,3,4,5)",a.toString());
		
		a.add(1);
		boolean ok = a.removeFirstOccurrence(1);
		assertTrue(ok);
		assertEquals("(2,3,4,5,1)", a.toString());
		
		a.push(1);
		ok = a.removeLastOccurrence(1);
		assertTrue(ok);
		assertEquals("(1,2,3,4,5)", a.toString());
		
		ok = a.removeFirstOccurrence(7);
		assertFalse(ok);
		
		ok = a.removeLastOccurrence(19);
		assertFalse(ok);
		
		//Test differences between throwing and returning null methods
		a.clear();
		
		assertEquals(null, a.peek());
		assertEquals(null, a.peekFirst());
		assertEquals(null, a.peekLast());
		
		assertEquals(null, a.poll());
		assertEquals(null, a.pollFirst());
		assertEquals(null, a.pollLast());
		
		shouldFail( (Supplier<Integer>) a::getFirst, ArrayIndexOutOfBoundsException.class);
		shouldFail( (Supplier<Integer>) a::getLast, ArrayIndexOutOfBoundsException.class);
		shouldFail( (Supplier<Integer>) a::removeFirst, ArrayIndexOutOfBoundsException.class);
		shouldFail( (Supplier<Integer>) a::removeLast, ArrayIndexOutOfBoundsException.class);
	}
}
