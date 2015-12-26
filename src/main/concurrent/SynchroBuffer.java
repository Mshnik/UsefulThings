package concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class SynchroBuffer {

  private final Map<Thread, String> threadToKeyMap;

  public SynchroBuffer() {
    threadToKeyMap = Collections.synchronizedMap(new HashMap<>());
  }

  public void assignKey(String arg) throws RuntimeException {
    if(validateKey(arg)) {
      threadToKeyMap.put(Thread.currentThread(), arg);
    } else {
      throw new RuntimeException("Can't assign " + Thread.currentThread() + " as type " + arg + " for " + this);
    }
  }

  public String getKey() {
    return threadToKeyMap.get(Thread.currentThread());
  }

  protected abstract boolean validateKey(String arg);

  public abstract void waitUntilReady() throws InterruptedException;

}
