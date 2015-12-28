package concurrent;

//TODO - SPEC
public class Turnstile extends SynchroBuffer{

  private final int workerCount;
  private int round;
  private int workersCompleted;
  private final Object mux;

  public Turnstile(int workerCount) throws IllegalArgumentException {
    if(workerCount <= 0) {
      throw new IllegalArgumentException();
    }
    this.workerCount = workerCount;
    round = 0;
    workersCompleted = 0;
    mux = new Object();
  }

  public int getWorkerCount() {
    return workerCount;
  }

  @Override
  protected boolean validateKey(String arg) {
    return true; //Keys unused in Turnstile, so all are valid.
  }

  public void waitUntilReady() throws InterruptedException {
    synchronized (mux) {
      workersCompleted++;
      int roundLocal = round;
      if (workersCompleted == workerCount * (roundLocal + 1)) {
        round++;
        mux.notifyAll();
      } else {
        while (workersCompleted < workerCount * (roundLocal + 1)) {
          mux.wait();
        }
      }
    }
  }
}
