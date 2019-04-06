package concurrent.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@link Runnable} class representing an Event hub. Callers (in other threads) can call this to
 * post events, which will be processed asynchronously by the wrapped thread. Events are processed
 * in the order they are added via either {@link EventProcessor#postEventAsync(Event)} or {@link
 * EventProcessor#postEventSync(Event)}.
 *
 * <p>{@link EventListener}s can be added to listen on the processing done by this class.
 *
 * <p>Once killed, an EventProcessor cannot be safely restarted - construct a new one instead.
 */
public abstract class EventProcessor<E extends Event, L extends EventListener<E>>
    implements Runnable {
  private final List<E> events = Collections.synchronizedList(new ArrayList<>());
  private final Set<E> syncEvents = Collections.synchronizedSet(new HashSet<>());
  private final List<L> listeners = Collections.synchronizedList(new ArrayList<>());

  // True after the run() method has completed, false until then.
  private boolean shutdown = false;
  private final Object shutdownMutex = new Object();

  // The thread running this processor. Null before this has started.
  private Thread thread;

  /** Returns the list of listeners attached to this. */
  public List<L> getListeners() {
    return Collections.unmodifiableList(listeners);
  }

  /**
   * Adds the given listener to this. Can be called if this is already running, but may result in
   * indeterminate behavior due to threading.
   */
  public void addListener(L listener) {
    listeners.add(listener);
  }

  /**
   * Posts the given event to this EventProcessor and immediately returns. The event will be
   * processed asynchronously with this call, so this is a lightweight call. Events added via this
   * method are processed in order.
   */
  public void postEventAsync(E event) {
    synchronized (events) {
      events.add(event);
      events.notifyAll();
    }
  }

  /**
   * Posts the given event to this EventProcessor and blocks on the given event being processed. The
   * event will be processed (synchronously) with this call, so this is a heavyweight call. Events
   * added via this method are processed in order. Because this method blocks on the given event
   * processing, it also effectively blocks on earlier methods being processed. Thus, use with
   * caution.
   */
  public void postEventSync(E event) throws InterruptedException {
    synchronized (syncEvents) {
      synchronized (events) {
        events.add(event);
        events.notifyAll();
        syncEvents.add(event);
      }
      while (syncEvents.contains(event)) {
        syncEvents.wait();
      }
    }
  }

  /** Returns the next event in the queue, blocking until there is one. */
  private E getNextEvent() throws InterruptedException {
    synchronized (events) {
      while (events.isEmpty()) {
        events.wait();
      }
      return events.remove(0);
    }
  }

  /** Process the given event. */
  protected abstract void process(E event);

  /**
   * Called when the running thread is interrupted. Expected when this is shutting down, so can be
   * used for internal cleanup.
   */
  protected abstract void threadInterrupted(InterruptedException exception);

  /**
   * Runs this EventProcessor loop. Executes the following:
   *
   * <ol>
   *   <li>Gets the next event
   *   <li>Tells listeners that it's about to process.
   *   <li>Processes event.
   *   <li>If the event was synchronous, wakes waiting callers.
   *   <li>Tells listeners that it processed.
   * </ol>
   *
   * Runs forever until {@link #interrupt()} is called.
   */
  @Override
  public void run() {
    try {
      while (true) {
        E event = getNextEvent();
        listeners.forEach(l -> l.willProcessEvent(event));
        process(event);
        synchronized (syncEvents) {
          if (syncEvents.remove(event)) {
            syncEvents.notifyAll();
          }
        }
        listeners.forEach(l -> l.eventProcessed(event));
      }
    } catch (InterruptedException e) {
      threadInterrupted(e);
    } finally {
      synchronized (shutdownMutex) {
        shutdown = true;
        shutdownMutex.notifyAll();
      }
    }
  }

  /** Starts this EventProcessor in a new thread. */
  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  /**
   * Kills this EventProcessor and immediately returns. The EventProcessor may run for a little
   * while after this call, so consider following with {@link #waitForShutdown()} if the caller
   * shouldn't continue until the EventProcessor is truly shut down.
   */
  public void interrupt() {
    if (thread != null) {
      thread.interrupt();
    } else {
      throw new RuntimeException("No thread reference to interrupt");
    }
  }

  /**
   * Blocks on this event processor to be shut down. If the caller is expected to be the one doing
   * the shut down, call {@link #interrupt()} first.
   */
  public void waitForShutdown() throws InterruptedException {
    if (thread == null) {
      throw new RuntimeException("Can't wait for shutdown, no thread reference");
    }
    synchronized (shutdownMutex) {
      while (!shutdown) {
        shutdownMutex.wait();
      }
    }
  }
}
