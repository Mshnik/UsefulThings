package concurrent;

/**
 * Threads that wish to have their progress monitored in some fashion may
 * implement the Monitorable interface to do so. This allows a TimeRemainingWorker
 * to monitor its progress and periodically send time remaining updates to the thread.
 * These updates can be displayed on a GUI or simply used for internal calculation.
 *
 * @author MPatashnik
 */
public interface Monitorable {

  /**
   * Called whenever the TimeRemainingWorker wants to calculate a new
   * amount of time remaining.
   * Should be calculated internally; timeRemainingWorker relies on return of this.
   *
   * @return - An amount of progress this thread has made since it has started its task.
   */
  public double getCompletionValue();

  /**
   * Tells the monitorable that the associated TimeRemainingWorker has updated its completion values.
   * (Has a new guess of how long until task completion)
   * all updates can be ignored simply by leaving the method body for this implementation empty.
   * To get updated values, see instance methods:
   * TimeRemainingWorker.getTimeRemaining()
   * TimeRemainingWorker.getPercentComplete()
   */
  public void update();

  /**
   * A succinct name for this task. Can just be the toString if that is short
   */
  public String getName();

}
