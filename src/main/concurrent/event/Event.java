package concurrent.event;

/**
 * An interface for an object passed as an event to an {@link EventProcessor}. Events should be
 * immutable + stable hashed, and ideally don't need references to outside state.
 */
public interface Event {}
