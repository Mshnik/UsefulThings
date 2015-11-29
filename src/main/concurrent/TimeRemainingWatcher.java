package concurrent;

/**
 * Created by Mshnik on 9/28/15.
 */
public interface TimeRemainingWatcher {

  /**
   * Tells the monitorable that the associated TimeRemainingWorker has updated its completion values.
   * (Has a new guess of how long until task completion)
   * all updates can be ignored simply by leaving the method body for this implementation empty.
   * Shouldn't block. If processing is needed, store the value and spawn a thread to do it.
   * To get updated values, see instance methods:
   * TimeRemainingWorker.getTimeRemaining()
   * TimeRemainingWorker.getPercentComplete()
   */
  void update(double completionProgress);
}
