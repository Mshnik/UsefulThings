package concurrent;

import java.util.ArrayList;

//TODO - SPEC
//TODO - TEST
public class NWayJoin extends SynchroBuffer {

  private ArrayList<String> ids;
  private Object[] locks;

  private final Object wayInCondition;
  private final Object wayOutCondition;
  private int typesArrived;
  private int typesLeaving;

  public NWayJoin(String... typeIDs) {
    ids = new ArrayList<>();
    locks = new Object[typeIDs.length];
    for(int i = 0; i < locks.length; i++) {
      ids.add(typeIDs[i]);
      locks[i] = new Object();
    }
    wayInCondition = new Object();
    wayOutCondition = new Object();
    typesArrived = 0;
    typesLeaving = 0;
  }

  @Override
  protected boolean validateKey(String arg) {
    return ids.contains(arg);
  }

  @Override
  public void waitUntilReady() throws InterruptedException {
    String key = getKey();
    if(key == null) {
      throw new RuntimeException("Can't wait in " + this + " until assignKey is called for this Thread");
    }
    int index = ids.indexOf(key);
    synchronized (locks[index]) {
      synchronized (wayInCondition) {
        typesArrived++;
        if (typesArrived == locks.length) {
          typesLeaving = 0;
          wayInCondition.notifyAll();
        } else {
          while (typesArrived < locks.length) {
            wayInCondition.wait();
          }
        }
      }
      synchronized (wayOutCondition) {
        typesLeaving++;
        if (typesLeaving == locks.length) {
          typesArrived = 0;
          wayOutCondition.notifyAll();
        } else {
          while (typesLeaving < locks.length) {
            wayOutCondition.wait();
          }
        }
      }
    }
  }
}
