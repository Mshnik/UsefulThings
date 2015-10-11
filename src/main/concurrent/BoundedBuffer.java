package concurrent;

import common.dataStructures.DeArrList;
import java.util.concurrent.Semaphore;

public class BoundedBuffer<T> {

  public static final int DEFAULT_CAPACITY = 16;

  private DeArrList<T> buffer;

  private Semaphore empty;
  private Semaphore full;
  private Semaphore arrAccess;

  public BoundedBuffer() {
    this(DEFAULT_CAPACITY);
  }

  public BoundedBuffer(int capacity) {
    buffer = new DeArrList<>(capacity);
    arrAccess = new Semaphore(1);
    empty = new Semaphore(capacity);
    full = new Semaphore(0);
  }

  public boolean add(T elm) throws InterruptedException {
    return add(elm, true);
  }

  public boolean add(T elm, boolean wait) throws InterruptedException {
    if(wait) {
      empty.acquire();
    } else {
      if(! empty.tryAcquire()) {
        return false;
      }
    }
    arrAccess.acquire();
    buffer.add(elm);
    arrAccess.release();
    full.release();
    return true;
  }

  public T get() throws InterruptedException {
    return get(true);
  }

  public T get(boolean wait) throws InterruptedException {
    if(wait) {
      full.acquire();
    } else {
      if(! full.tryAcquire()) {
        return null;
      }
    }
    arrAccess.acquire();
    T elm = buffer.removeFirst();
    arrAccess.release();
    full.release();
    return elm;
  }

  public int size() {
    try{
      arrAccess.acquire();
    }catch(InterruptedException e) {
      return -1;
    }
    int s = buffer.size();
    arrAccess.release();
    return s;
  }

}
