package concurrent;

import functional.Supplier;

/** MethodRunner allows a method call to be made along with a timeout time.
 * The method call is made in a newly created thread while the calling thread waits for it to
 * finish. If the method call terminates before the timeout time, the value is simply returned.
 * If, however, the timeout time arrives without the method call finished, the calling thread is
 * simply terminated in an unsafe state. This was done in an effort to make MethodRunner simplistic
 * and easy to use, but has concequences if methods with side-effects.
 * @param <T> - the return type of the method call
 * @author Mshnik
 */
public class MethodRunner<T> {

  private final Supplier<T> methodCall;
  private T result;
  private boolean done;
  private final long millisToWait;

  private static final long MILLIS_SLEEP_BETWEEN_UPDATES = 100;

  /** Construct a new MethodRunner
   * @param methodCall - the method call to make
   * @param millisToWait - the number of milliseconds to wait for methodCall to return
   * @throws IllegalArgumentException - if methodCall == null, or millisToWait <= 0
   */
  public MethodRunner(Supplier<T> methodCall, long millisToWait) throws IllegalArgumentException {
    if(methodCall == null) {
      throw new IllegalArgumentException("Can't create timeout worker on null method call");
    }
    if(millisToWait <= 0) {
      throw new IllegalArgumentException("Can't wait a non-positive number of milliseconds");
    }

    this.methodCall = methodCall;
    this.millisToWait = millisToWait;
  }

  /** Runs the method call for up to {@code millisToWait} milliseconds,
   * then returns the result. If the call was interrupted, null will be returned.
   * This method can be invoked multiple times to make the same method call multiple times,
   * but the result of the first call will be forgotten upon making the second call.
   */
  public T runAndGet() {
    done = false;
    result = null;
    long startTime = System.currentTimeMillis();
    TimeoutWorker r = new TimeoutWorker();
    r.start();
    while (System.currentTimeMillis() < startTime + millisToWait && !done) {
      try {
        Thread.sleep(MILLIS_SLEEP_BETWEEN_UPDATES);
      } catch (InterruptedException e) {
          return null; //Give up on result
      }
    }

    if(!done) {
      r.stop();
    }
    done = true;
    return result;
  }

  /** If his method call has not been run, calls runAndGet() to compute and return it.
   * If it has, returns the result that was computed.
   * Thus multiple calls to get() will compute the result once and then return it for each call.
   */
  public T get() {
    if (done) {
      return result;
    } else {
      return runAndGet();
    }
  }

  /** A helper class for MethodRunner - simply executes the method call and sets done to
   * true upon termination
   * @author Mshnik
   */
  private class TimeoutWorker extends Thread {

    @Override
    public void run() {
      result = methodCall.apply();
      done = true;
    }

    @Override
    public String toString() {
      return "Timeout worker for " + TimeoutWorker.this;
    }
  }

}
