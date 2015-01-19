package grid;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class GridTest {

	static class IntTile implements Tile{
		private final Integer[] loc;
		public final int val;
		
		public IntTile(Integer[] loc, int v){
			this.loc = loc;
			this.val = v;
		}
		
		@Override
		public Integer[] getLocation() {
			return Arrays.copyOf(loc, loc.length);
		}
		
		public String toString(){
			return val + "@" + Arrays.deepToString(loc);
		}
	}
	
	private void checkInvariants(Grid<?> g){
		assertEquals(g.dimension, g.getBounds().length);
		Collection<?> c = g.getAll();
		assertEquals(g.size(), c.size());
	}
	
	@Test
	public void testConstruction() {
		
		Grid<IntTile> g = new Grid<>();
		checkInvariants(g);
		assertEquals(g.dimension, 0);
		assertTrue(g.isEmpty());
		
		Grid<IntTile> g2 = new Grid<>(5);
		checkInvariants(g2);
		assertEquals(1, g2.dimension);
		assertEquals(5, g2.getBounds()[0]);
		assertTrue(g2.isEmpty());
		
		
		Integer[] d = {1, 2, 3, 4, 5};
		Grid<IntTile> g3 = new Grid<>(d);
		checkInvariants(g3);
		assertEquals(d.length, g3.dimension);
		for(int i = 0; i < d.length; i++){
			assertEquals(d[i].intValue(), g3.getBounds()[i]);
		}
		assertTrue(g3.isEmpty());
	}
	
	@Test
	public void testAddition(){
		Grid<IntTile> g = new Grid<>(10, 10);
		checkInvariants(g);
		
		IntTile t = new IntTile(new Integer[]{0, 0}, 2);
		boolean ok = g.add(t);
		
		checkInvariants(g);
		assertTrue(ok);
		assertTrue(g.contains(t));
		assertEquals(1, g.size());
		
		ok = g.add(t);
		
		checkInvariants(g);
		assertTrue(! ok); //Re-adding t should be false
		assertTrue(g.contains(t));
		assertEquals(1, g.size());
		
		IntTile t2 = new IntTile(new Integer[]{0, 1}, 4);
		ok = g.add(t2);
		
		checkInvariants(g);
		assertTrue(ok);
		assertTrue(g.contains(t));
		assertTrue(g.contains(t2));
		assertEquals(2, g.size());
		
		//Overwrite previous tile
		IntTile t3 = new IntTile(new Integer[]{0, 0}, 5);
		ok = g.add(t3);
		
		checkInvariants(g);
		assertTrue(ok);
		assertTrue(! g.contains(t));
		assertTrue(g.contains(t3));
		assertTrue(g.contains(t2));
		assertEquals(2, g.size());
		
		//Add OOB
		ok = false;
		try{
			g.add(new IntTile(new Integer[]{11, 5}, 2));
		}catch(ArrayIndexOutOfBoundsException e){
			ok = true;
		}
		assertTrue(ok);
		checkInvariants(g);
		assertEquals(2, g.size());
	}
	
	@Test
	public void testRemove(){
		Grid<IntTile> g = new Grid<>(10, 10);
		
		IntTile t = new IntTile(new Integer[]{0, 0}, 3);
		g.add(t);
		g.add(new IntTile(new Integer[]{0, 1}, 4));
		
		checkInvariants(g);
		assertEquals(2, g.size());
		
		IntTile t2 = g.remove(0, 0);
		
		checkInvariants(g);
		assertEquals(1, g.size());
		assertEquals(t, t2);
		assertTrue(! g.containsAt(0, 0));
		
		g.clear();
		checkInvariants(g);
		assertEquals(0, g.size());
	}
	
	@Test
	public void testConversion(){
		Grid<IntTile> g = new Grid<>(10, 10);
		
		IntTile t = new IntTile(new Integer[]{0, 0}, 1);
		IntTile t2 = new IntTile(new Integer[]{0, 1}, 2);
		IntTile t3 = new IntTile(new Integer[]{1, 0}, 3);
		IntTile t4 = new IntTile(new Integer[]{1, 1}, 4);
		
		g.add(t);
		g.add(t2);
		g.add(t3);
		g.add(t4);
		
		IntTile[] a = {t, t2, t3, t4};
		for(int i = 0; i < g.size(); i++){
			assertEquals(a[i], g.toArray(new IntTile[0])[i]);
		}
		
		HashMap<List<Integer>, IntTile> m = new HashMap<>();
		for(IntTile x : a){
			m.put(Arrays.asList(x.getLocation()), x);
		}
		
		assertEquals(m, g.toMap());
	}
	
	@Test
	public void testEqualityAndHashing(){
		Grid<IntTile> g = new Grid<>(10, 10);
		Grid<IntTile> g2 = new Grid<>(10, 10);
		Grid<IntTile> g3 = new Grid<>(5, 5); //oddball
		
		assertTrue(g.equals(g2));
		assertTrue(g.hashCode() == g2.hashCode());
		assertTrue(! g.equals(g3));
		assertTrue(g.hashCode() != g3.hashCode());
		
		IntTile t = new IntTile(new Integer[]{0, 0}, 2);
		g.add(t);
		
		assertTrue(! g.equals(g2));
		assertTrue( g.hashCode() != g2.hashCode());
		
		g2.add(t);
		g3.add(t);
		
		assertTrue(g.equals(g2));
		assertTrue(g.hashCode() == g2.hashCode());
		assertTrue(! g.equals(g3));
		assertTrue(g.hashCode() != g3.hashCode());
		
		assertEquals(g, g.clone());
		assertEquals(g3, g3.clone());
		assertEquals(g3, g.clone(5,5));
	}

}
