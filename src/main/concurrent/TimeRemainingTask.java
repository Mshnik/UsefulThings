package concurrent;

/**
 * Processees that wish to have their progress monitored in some fashion may
 * implement the TimeRemainingTask interface to do so. This allows a TimeRemainingWorker
 * to monitor its progress and periodically send time remaining updates to the thread.
 * These updates can be displayed on a GUI or simply used for internal calculation.
 *
 * @author MPatashnik
 */
public interface TimeRemainingTask {

  /**
   * Called whenever the TimeRemainingWorker wants to calculate a new
   * amount of time remaining.
   * Should be calculated internally; timeRemainingWorker relies on return of this.
   *
   * @return - An amount of progress this thread has made since it has started its task.
   */
  double getCompletionValue();

  /**
   * A succinct name for this task. Can just be the toString if that is short
   */
  String getName();

}
