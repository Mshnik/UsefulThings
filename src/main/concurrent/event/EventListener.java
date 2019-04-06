package concurrent.event;

/**
 * A listener for events on a {@link EventProcessor}. The same events that are passed to the
 * processor will be passed to each event listener, before and after the event is processed.
 * Listeners are called in the order they are attached to the processor.
 *
 * <p>As listen events are called synchronously within the processor, they should not take too much
 * time. Heavy lifting should be done by posting an event to a different processor.
 */
public interface EventListener<E extends Event> {
  /** Listen to an event that will be processed next. The event has not yet been processed. */
  void willProcessEvent(E event);

  /** Listen to an event that was processed. The event has just been processed. */
  void eventProcessed(E event);
}
