package common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;


public class UtilTest {
	
	@Test
	public void testWrappers(){
		short[] s = {1,2,3,4};
		Short[] s2 = Util.boxArr(s);
		short[] s3 = Util.unboxArr(s2);
		
		assertEquals(s.length, s2.length);
		assertEquals(s.length, s3.length);
		for(int i = 0; i < s.length; i++){
			assertEquals(s[i], s2[i].shortValue());
			assertEquals(s[i], s3[i]);
		}
		
		int[] i = {1,2,3,4, Integer.MAX_VALUE, Integer.MIN_VALUE};
		Integer[] i2 = Util.boxArr(i);
		int[] i3 = Util.unboxArr(i2);
		
		assertEquals(i.length, i2.length);
		assertEquals(i.length, i3.length);
		for(int x = 0; x < s.length; x++){
			assertEquals(i[x], i2[x].intValue());
			assertEquals(i[x], i3[x]);
		}
		
		long[] l = {1,2,3,4, Integer.MAX_VALUE, Integer.MIN_VALUE, Long.MAX_VALUE, Long.MIN_VALUE};
		Long[] l2 = Util.boxArr(l);
		long[] l3 = Util.unboxArr(l2);
		
		assertEquals(l.length, l2.length);
		assertEquals(l.length, l3.length);
		for(int x = 0; x < s.length; x++){
			assertEquals(l[x], l2[x].longValue());
			assertEquals(l[x], l3[x]);
		}
		
		float[] f = {1,2,3,4, 0.4f, 0.1f, Float.MAX_VALUE, Float.MIN_NORMAL};
		Float[] f2 = Util.boxArr(f);
		float[] f3 = Util.unboxArr(f2);
		
		assertEquals(f.length, f2.length);
		assertEquals(f.length, f3.length);
		for(int x = 0; x < s.length; x++){
			assertEquals(f[x], f2[x].floatValue(), 0.00001);
			assertEquals(f[x], f3[x], 0.00001);
		}
		
		
		double[] d = {1,2,3,4, 0.4, 0.1, Float.MAX_VALUE, Float.MIN_VALUE, Double.MAX_VALUE, Double.MIN_NORMAL};
		Double[] d2 = Util.boxArr(d);
		double[] d3 = Util.unboxArr(d2);
		
		assertEquals(d.length, d2.length);
		assertEquals(d.length, d3.length);
		for(int x = 0; x < s.length; x++){
			assertEquals(d[x], d2[x].doubleValue(), 0.00001);
			assertEquals(d[x], d3[x], 0.00001);
		}
		
		char[] c = {1,2,3,4, 'a', 'b', '.', Character.MAX_VALUE, Character.MIN_VALUE};
		Character[] c2 = Util.boxArr(c);
		char[] c3 = Util.unboxArr(c2);
		
		assertEquals(c.length, c2.length);
		assertEquals(c.length, c3.length);
		for(int x = 0; x < s.length; x++){
			assertEquals(c[x], c2[x].charValue());
			assertEquals(c[x], c3[x]);
		}
	}
	
	@Test
	public void testRandElement(){
		assertTrue(null==Util.randomElement(null));
		
		HashSet<Integer> s = new HashSet<>();
		assertTrue(null==Util.randomElement(s));
		
		for(int i = 0; i < 10; i++){
			s.add(i);
		}
		
		HashSet<Integer> s2 = new HashSet<>();
		while(! s2.equals(s)){
			Integer i = Util.randomElement(s);
			s2.add(i);
			assertTrue(s.contains(i));
		}
	}

	@Test
	public void testPermute(){
		ArrayList<Integer[]> a = Util.permute(new Integer[]{1,2,3,4});
		assertEquals(a.size(), 24);
		
		for(Integer[] i : a){
			assertEquals(i.length, 4);
			assertEquals(10, i[0] + i[1] + i[2] + i[3]);
		}
		
		HashSet<Integer[]> s = new HashSet<>(a);
		assertEquals(s.size(), 24);
		
		a = Util.permute(new Integer[]{});
		assertEquals(a.size(), 1);
	}
	
}
