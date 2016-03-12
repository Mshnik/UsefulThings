package common.dataStructures;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static common.JUnitUtil.assertEquals;
import static common.JUnitUtil.fail;
import static common.JUnitUtil.shouldFail;

/**
 * @author Mshnik
 */
public class PoolTest {

  private static class PoolableInt implements Pool.Poolable {

    private static int initCount;
    private int x;

    public PoolableInt() {
      x = 2; //Should be reset when created by pool
      initCount++;
    }

    @Override
    public void reset() {
      x = 0;
    }
  }

  public static int initCount() {
    return PoolableInt.initCount;
  }

  @Before
  public void resetInitCount() {
    PoolableInt.initCount = 0;
  }

  @Test
  public void testConstructionAndGet() {
    assertEquals(0, initCount());

    final int capacity = 10;
    Pool<PoolableInt> pool = new Pool<>(PoolableInt::new, capacity);
    assertEquals(capacity, initCount());

    for(int i = 0; i < capacity; i++) {
      assertEquals(capacity, initCount());
      assertEquals(capacity - i, pool.countRemaining());
      PoolableInt p = pool.getOrNull();
      assertEquals(0, p.x);
    }

    assertEquals(capacity, initCount());
    assertEquals(0, pool.countRemaining());
  }

  @Test
  public void testGetAndPut() {
    final int capacity = 10;
    Pool<PoolableInt> pool = new Pool<>(PoolableInt::new, capacity);

    List<PoolableInt> lst = new ArrayList<>();
    for(int i = 0; i < capacity; i++) {
      assertEquals(capacity, initCount());
      assertEquals(capacity - i, pool.countRemaining());
      PoolableInt p = pool.getOrNull();
      p.x = i+1;
      lst.add(p);
    }

    assertEquals(capacity, initCount());
    assertEquals(0, pool.countRemaining());
    assertEquals(capacity, lst.size());

    for(int i = 0; i < capacity; i++) {
      PoolableInt p =  lst.remove(0);
      assertEquals(i+1, p.x);
      pool.discard(p);
      assertEquals(i+1, pool.countRemaining());
      assertEquals(0, p.x);
      assertEquals(capacity, initCount());
    }
  }

  @Test
  public void testGetBehaviors() {
    final int capacity = 0;
    Pool<PoolableInt> pool = new Pool<>(PoolableInt::new, capacity);

    assertEquals(null, pool.getOrNull());
    shouldFail(pool::getOrThrow, Pool.EmptyPoolException.class);
    assertEquals(0, pool.getOrNew().x);

    assertEquals(null, pool.get());
    pool.setPoolEmptyBehavior(null);
    assertEquals(null, pool.get());
    pool.setPoolEmptyBehavior(Pool.PoolEmptyBehavior.NULL);
    assertEquals(null, pool.get());
    pool.setPoolEmptyBehavior(Pool.PoolEmptyBehavior.THROW);
    shouldFail(pool::get, Pool.EmptyPoolException.class);
    pool.setPoolEmptyBehavior(Pool.PoolEmptyBehavior.NEW);
    assertEquals(0, pool.get().x);
  }
}
