package functional;

import static org.junit.Assert.*;

import org.junit.Test;

public class FunctionalTest {
	
	@Test
	public void testCurrying(){
		TriFunction<Integer, Integer, Integer, Integer> f3 = (a,b,c) -> a + b * c;
		BiFunction<Integer, Integer, Integer> f3_c1 = f3.partialApply(5);
		BiFunction<Integer, Integer, Integer> f2 = (a,b) -> 5 + a*b;
		
		for(int i = -5; i < 5; i++) {
			for(int j = -5; j < 5; j++) {
				assertEquals(f2.apply(i, j), f3_c1.apply(i, j));
			}
		}
		
		Function<Integer, Integer> f3_c2 = f3.partialApply(5, 3);
		Function<Integer, Integer> f2_c1 = f2.partialApply(3);
		Function<Integer, Integer> f1 = (a) -> 5 + 3 * a;
		
		for(int i = -5; i < 5; i++) {
			assertEquals(f1.apply(i), f2_c1.apply(i));
			assertEquals(f1.apply(i), f3_c2.apply(i));
		}
		
		Supplier<Integer> f3_c3 = f3.partialApply(5, 3, 2);
		Supplier<Integer> f2_c2 = f2.partialApply(3, 2);
		Supplier<Integer> f1_c1 = f1.partialApply(2);
		Supplier<Integer> s = () -> 11;
		
		assertEquals(s.apply(), f3_c3.apply());
		assertEquals(s.apply(), f2_c2.apply());
		assertEquals(s.apply(), f1_c1.apply());
	}
	
	@Test
	public void testAndThen() {
		Function<Integer, Integer> f1 = (a) -> (a + 2);
		Function<Integer, Integer> f2 = (a) -> -a;
		
		assertEquals(-5, f1.andThen(f2).apply(3).intValue());
		assertEquals(-1, f2.andThen(f1).apply(3).intValue());
		assertEquals(1, f2.andThen(f1).andThen(f1).apply(3).intValue());
	}
	
	@Test
	public void testPredicateLogic() {
		Predicate<Integer> p1 = (a) -> a > 5;
		Predicate<Integer> p2 = (a) -> a < 8;
		
		assertTrue(p1.apply(8));
		assertFalse(p1.apply(5));
		assertTrue(p2.apply(3));
		assertFalse(p2.apply(9));
		
		assertTrue(p1.negate().apply(3));
		assertFalse(p1.negate().apply(7));
		
		assertTrue(p1.negate().negate().apply(7));
		
		assertTrue(p1.and(p2).apply(6));
		assertFalse(p1.and(p2).apply(4));
		assertFalse(p1.and(p2).apply(9));
		
		assertTrue(p1.and(p2.negate()).apply(10));
	}

}
