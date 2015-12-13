package concurrent;

import common.types.Either;
import common.types.Left;
import common.types.Right;
import functional.impl.Function2;
import functional.impl.Supplier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadMaster<R> {

  private AtomicInteger nextSlaveID;
  private Supplier<R> methodCall;
  private Map<Integer, Either<Throwable, R>> results;

  private AtomicInteger workersStarted;
  private AtomicInteger workersFinished;
  private final Object workersDoneCondition;

  public ThreadMaster(){
    this(null);
  }

  public ThreadMaster(Supplier<R> defaultMethodCall) {
    nextSlaveID = new AtomicInteger();
    methodCall = defaultMethodCall;
    results = new HashMap<>();
    workersStarted = new AtomicInteger();
    workersFinished = new AtomicInteger();
    workersDoneCondition = new Object();
  }

  private class ThreadSlave extends Thread {
    private Supplier<R> methodCall;
    private int id;

    public ThreadSlave(Supplier<R> methodCall) {
      this.methodCall = methodCall;
      id = nextSlaveID.getAndIncrement();
    }

    public void run() {
      Either<Throwable, R> result = null;
      try {
        result = new Right<>(methodCall.apply());
      } catch(Throwable t) {
        result = new Left<>(t);
      } finally {
        synchronized (workersDoneCondition) {
          results.put(id, result);
          workersFinished.incrementAndGet();
          workersDoneCondition.notify();
        }
      }
    }

    public String toString() {
      return "Worker thread for " + ThreadMaster.this.toString();
    }
  }

//  private class TimeoutWorker extends Thread {
//    private ThreadSlave slave;
//    private long millisToWait;
//
//    private TimeoutWorker(ThreadSlave s, long millisToWait) {
//      slave = s;
//      this.millisToWait = millisToWait;
//    }
//
//    public void run() {
//      slave.start();
//      try {
//        Thread.sleep(millisToWait);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//      if (! slave.isAlive()) {
//        slave.interrupt();
//      }
//    }
//  }


  /** Runs the method call for up to {@code millisToWait} milliseconds,
   * then returns the result. If the call was interrupted, null will be returned.
   * This method can be invoked multiple times to make the same method call multiple times,
   * but the result of the first call will be forgotten upon making the second call.
   *
   * @return the result if it was computed, the throwable if an exception or error occurred, or null if
   *    the call was terminated due to timeout or if the worker thread was somehow interrupted.
   */

  public int getNextSlaveID() {
    return nextSlaveID.get();
  }

  public int spawnWorker(Supplier<R> task) {
    ThreadSlave t = new ThreadSlave(task);
    workersStarted.incrementAndGet();
    t.start();
    return t.id;
  }

  public int spawnWorker() {
    return spawnWorker(methodCall);
  }

  public void waitForWorkers() throws InterruptedException {
    synchronized (workersDoneCondition) {
      while(workersStarted.get() > workersFinished.get()) {
        workersDoneCondition.wait();
      }
    }
  }

  public void waitForWorkers(int... ids) throws InterruptedException {
    synchronized (workersDoneCondition) {
      while(true) {
        boolean missing = false;
        for(int id : ids) {
          if(! results.containsKey(id)){
            missing = true;
            break;
          }
        }
        if (! missing) {
          break;
        }
        workersDoneCondition.wait();
      }
    }
  }

  public void waitForWorker(int id) throws InterruptedException {
    synchronized (workersDoneCondition) {
      while(! results.containsKey(id)) {
        workersDoneCondition.wait();
      }
    }
  }

  public void reset() throws InterruptedException {
    synchronized (workersDoneCondition) {
      while (workersStarted.get() > workersFinished.get()) {
        workersDoneCondition.wait();
      }
      results.clear();
      nextSlaveID.set(0);
      workersStarted.set(0);
      workersFinished.set(0);
    }
  }

  public Map<Integer, Either<Throwable, R>> getResults() {
    return new HashMap<>(results);
  }

  public Either<Throwable, R> getResult(int id) {
    return results.get(id);
  }

  public <X> X reduceResults(X initial, Function2<X, Either<Throwable, R>, X> f) {
    for(Either<Throwable, R> e : results.values()) {
      initial = f.apply(initial, e);
    }
    return initial;
  }
}
