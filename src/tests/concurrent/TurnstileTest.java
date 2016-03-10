package concurrent;


import functional.impl.Function2;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static common.JUnitUtil.*;

public class TurnstileTest {

  private Function2<Turnstile, AtomicInteger, Integer> method = (t, a2) -> {
    try {
      Thread.sleep((int)(50 * Math.random()));
      a2.incrementAndGet();
      t.waitUntilReady();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return a2.get();
  };

  private Function2<Turnstile, AtomicInteger, String> method2 = (t, a1) -> {
    String s = "";
    try {
      for(int i = 0; i < 5; i++) {
        Thread.sleep((int) (50 * Math.random()));
        a1.getAndIncrement();
        t.waitUntilReady();
        s += a1.get();
        t.waitUntilReady();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return s;
  };

  @Test
  public void testOneWorker() throws InterruptedException {
    Turnstile t = new Turnstile(1);
    ThreadMaster<Integer> master = new ThreadMaster<>();
    AtomicInteger finishCount = new AtomicInteger();

    int id = master.spawnWorker(method.partialApply(t, finishCount));
    master.waitForWorkers();
    assertEquals(1, master.getResult(id).asLeft());
  }

  @Test
  public void testMultipleWorkers() throws InterruptedException {
    Turnstile t = new Turnstile(5);
    ThreadMaster<Integer> master = new ThreadMaster<>();
    AtomicInteger finishCount = new AtomicInteger();

    int[] id = new int[5];
    for(int i = 0; i < id.length; i++) {
      id[i] = master.spawnWorker(method.partialApply(t, finishCount));
    }
    master.waitForWorkers();
    for(int i = 0; i < id.length; i++) {
      assertEquals(id.length, master.getResult(id[i]).asLeft());
    }
  }

  @Test
  public void testOneWorkerMultipleRounds() throws InterruptedException {
    Turnstile t = new Turnstile(1);
    ThreadMaster<String> master = new ThreadMaster<>();
    AtomicInteger finishCount = new AtomicInteger();

    int id = master.spawnWorker(method2.partialApply(t, finishCount));
    master.waitForWorkers();
    assertEquals("12345", master.getResult(id).asLeft());
  }

  @Test
  public void testMultipleWorkersMultipleRounds() throws InterruptedException {
    Turnstile t = new Turnstile(5);
    ThreadMaster<String> master = new ThreadMaster<>();
    AtomicInteger finishCount = new AtomicInteger();

    int[] id = new int[5];
    for(int i = 0; i < id.length; i++) {
      id[i] = master.spawnWorker(method2.partialApply(t, finishCount));
    }
    master.waitForWorkers();
    for(int i = 0; i < id.length; i++) {
      assertEquals("510152025", master.getResult(id[i]).asLeft());
    }
  }

}
