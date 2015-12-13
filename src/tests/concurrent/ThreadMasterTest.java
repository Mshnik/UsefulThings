package concurrent;

import common.types.Either;
import functional.impl.Function1;
import org.junit.Test;

import java.util.HashMap;

import static common.JUnitUtil.*;

public class ThreadMasterTest {

  @Test
  public void testSpawnWorkerAndWait() throws InterruptedException{
    ThreadMaster<Integer> t = new ThreadMaster<>();
    int id = t.spawnWorker(() -> 5);
    t.waitForWorker(id);
    assertEquals(5, t.getResult(id).asRight());

    int id2 = t.spawnWorker(() -> 6);
    t.waitForWorkers();
    assertEquals(5, t.getResult(id).asRight());
    assertEquals(6, t.getResult(id2).asRight());
  }

  @Test
  public void testDefaultMethodCall() throws InterruptedException {
    ThreadMaster<Integer> t = new ThreadMaster<>(() -> 5);
    int id = t.spawnWorker();
    t.waitForWorker(id);
    assertEquals(5, t.getResult(id).asRight());
  }

  @Test
  public void testTimedWaitingStress() throws InterruptedException {
    final int arrSize = 100;
    ThreadMaster<Integer> t = new ThreadMaster<>();
    Function1<Integer, Integer> f = (x) -> {
      try {
        Thread.sleep((long) (1000 * Math.random()));
      }catch(InterruptedException e){};
      return x;
    };
    int[] ids = new int[arrSize];
    int[] vals = new int[arrSize];
    for(int i = 0; i < arrSize; i++) {
      vals[i] = i;
      ids[i] = t.spawnWorker(f.partialApply(i));
    }

    t.waitForWorkers();
    for(int i = 0; i < arrSize; i++) {
      assertEquals(vals[i], t.getResult(ids[i]).asRight());
    }
  }

  @Test
  public void testReduce() throws InterruptedException {
    ThreadMaster<Integer> t = new ThreadMaster<>();
    Function1<Integer, Integer> f = (i) -> i;
    for(int i = 1; i <= 5; i ++) {
      t.spawnWorker(f.partialApply(i));
    }
    t.waitForWorkers();
    assertEquals(15, t.reduceResults(0, (a,b) -> a+b.asRight()));
  }

  @Test
  public void testReset() throws InterruptedException {
    ThreadMaster<Integer> t = new ThreadMaster<>();
    t.spawnWorker(() -> 4);
    t.waitForWorkers();
    t.reset();
    assertEquals(new HashMap<Integer, Either<Throwable, Integer>>(), t.getResults());
  }

}
