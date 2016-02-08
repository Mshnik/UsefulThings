package concurrent;

import common.MethodRunner;
import functional.impl.Supplier;
import org.junit.Test;

import static common.JUnitUtil.*;

public class UnboundedBufferTest {

  @Test
  public void testNonConcurrent() throws InterruptedException {
    UnboundedBuffer<Integer> b = new UnboundedBuffer<>();

    assertEquals(0, b.size());
    assertEquals("()", b.toString());
    b.add(5);
    assertEquals(1, b.size());
    assertEquals("(5)", b.toString());
    b.add(6);
    assertEquals(2, b.size());
    assertEquals("(5,6)", b.toString());

    assertEquals(5, b.get());
    assertEquals(1, b.size());
    assertEquals("(6)", b.toString());

    assertEquals(6, b.get());
    assertEquals(0, b.size());
    assertEquals("()", b.toString());
  }

  @Test
  public void testGetOnEmpty() {
    UnboundedBuffer<Integer> b = new UnboundedBuffer<>();
    MethodRunner<Integer> m = MethodRunner.of(() -> {
      try {
        return b.get();
      } catch (InterruptedException e) {
        return -1;
      }
    }).withWaitTime(100);

    assertEquals(null, m.get());
  }

  @Test
  public void testConcurrent() throws InterruptedException {
    UnboundedBuffer<Integer> b = new UnboundedBuffer<>();
    final int count = 50;
    ThreadMaster<Integer> t = new ThreadMaster<>();

    Supplier<Integer> s = () -> {
      try{
        Thread.sleep((int)(100 * Math.random()));
        b.add(1);
        return 1;
      }catch(InterruptedException e) {
        return null;
      }
    };
    Supplier<Integer> c = () -> {
      try{
        Thread.sleep((int)(100 * Math.random()));
        assertEquals(1, b.get());
        return 1;
      }catch(InterruptedException e) {
        return null;
      }
    };

    for(int i = 0; i < count; i++) {
      t.spawnWorker(s);
      t.spawnWorker(c);
    }

    t.waitForWorkers();
    assertEquals(0, b.size());
  }
}
