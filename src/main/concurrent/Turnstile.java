package concurrent;

public class Turnstile implements SynchroBuffer{

  private final int workerCount;
  private int round;
  private int workersCompleted;

  public Turnstile(int workerCount) throws IllegalArgumentException {
    if(workerCount <= 0) {
      throw new IllegalArgumentException();
    }
    this.workerCount = workerCount;
    round = 0;
    workersCompleted = 0;
  }

  public int getWorkerCount() {
    return workerCount;
  }

  public synchronized void waitUntilReady(String arg) throws InterruptedException {
    waitUntilReady();
  }

  public synchronized void waitUntilReady() throws InterruptedException {
    workersCompleted++;
    int roundLocal = round;
    if(workersCompleted == workerCount * (roundLocal+1)) {
      round++;
      notifyAll();
    } else {
      while (workersCompleted < workerCount * (roundLocal + 1)) {
        wait();
      }
    }
  }
}
