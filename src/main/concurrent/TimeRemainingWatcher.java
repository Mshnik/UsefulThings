package concurrent;

//TODO - SPEC
//TODO - TEST
public interface TimeRemainingWatcher {

  /**
   * Tells the TimeRemainingWatcher that the associated TimeRemainingWorker has updated its completion values.
   * (Has a new guess of how long until task completion)
   * all updates can be ignored simply by leaving the method body for this implementation empty.
   * Shouldn't block. If processing is needed, store the value and spawn a thread to do it.
   * To get updated values, see instance methods:
   * TimeRemainingWorker.getTimeRemaining()
   * TimeRemainingWorker.getPercentComplete()
   */
  void update(double completionProgress);
}
