package concurrent;

import common.dataStructures.DeArrList;

import java.util.concurrent.Semaphore;

public class UnboundedBuffer<T> {

  private DeArrList<T> buffer;
  private Semaphore itemsAvailable;

  public UnboundedBuffer() {
    buffer = new DeArrList<>();
    itemsAvailable = new Semaphore(0);
  }

  public void add(T t) {
    synchronized(this) {
      buffer.add(t);
      itemsAvailable.release();
    }
  }

  public T get() throws InterruptedException {
    itemsAvailable.acquire();
    synchronized (this) {
      return buffer.pop();
    }
  }

  public int size() {
    synchronized (this) {
      return buffer.size();
    }
  }

  public String toString() {
    return buffer.toString();
  }
}
