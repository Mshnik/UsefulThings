package common.dataStructures;

import functional.impl.Supplier;

/**
 * @author Mshnik
 * //TODO - spec
 */
public class Pool<T extends Pool.Poolable> {

  public static interface Poolable {
    public void reset();
  }

  public static class EmptyPoolException extends RuntimeException {}

  public enum PoolEmptyBehavior {
    NULL,
    NEW,
    THROW
  }
  private static final PoolEmptyBehavior DEFAULT_BEHAVIOR = PoolEmptyBehavior.NULL;

  public static final int DEFAULT_SIZE = 16;
  private DeArrList<T> freeObjs;
  private Supplier<T> constructor;
  private PoolEmptyBehavior poolEmptyBehavior;

  public Pool(Supplier<T> constructor) {
    this(constructor, DEFAULT_SIZE);
  }

  public Pool(Supplier<T> constructor, int capacity) {
    freeObjs = new DeArrList<>();
    freeObjs.ensureCapacity(capacity);
    this.constructor = constructor;
    for(int i = 0; i < capacity; i++) {
      freeObjs.add(createInstance());
    }
    setPoolEmptyBehavior(DEFAULT_BEHAVIOR);
  }

  public void setPoolEmptyBehavior(PoolEmptyBehavior b) {
    if(b == null) {
      b = DEFAULT_BEHAVIOR;
    }
    poolEmptyBehavior = b;
  }

  public PoolEmptyBehavior getPoolEmptyBehavior() {
    return poolEmptyBehavior;
  }

  private T createInstance() {
    T t = constructor.apply();
    t.reset();
    return t;
  }

  public int countRemaining() {
    return freeObjs.size();
  }

  public T get() throws EmptyPoolException {
    if(freeObjs.isEmpty()) {
      switch(poolEmptyBehavior) {
        case NULL:
          return null;
        case NEW:
          return createInstance();
        case THROW:
          throw new EmptyPoolException();
        default:
          throw new UnsupportedOperationException();
      }
    } else {
      return freeObjs.poll();
    }
  }

  public T getOrThrow() throws EmptyPoolException {
    if (freeObjs.isEmpty()) {
      throw new EmptyPoolException();
    } else {
      return freeObjs.poll();
    }
  }

  public T getOrNull() {
    return freeObjs.poll();
  }

  public T getOrNew() {
    if(freeObjs.isEmpty()) {
      return createInstance();
    } else {
      return freeObjs.poll();
    }
  }

  public void discard(T t) {
    t.reset();
    freeObjs.add(t);
  }

}
