package concurrent.event;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** @author Mshnik */
public class EventProcessorTest {
  private AddsToCollectionEventProcessor processor;

  @Before
  public void setup() {
    processor = new AddsToCollectionEventProcessor();
  }

  @Test
  public void isInterruptedAfterInterrupted() throws Exception {
    processor.start();
    assertFalse(processor.interrupted);
    processor.interrupt();
    processor.waitForShutdown();
    assertTrue(processor.interrupted);
  }

  @Test
  public void processesEvents() throws Exception {
    processor.start();
    processor.postEventAsync(new StringEvent("Foo1"));
    processor.postEventAsync(new StringEvent("Foo2"));
    processor.postEventAsync(new StringEvent("Foo3"));
    processor.interrupt();
    processor.waitForShutdown();
    assertEquals(
        processor.processedEvents,
        Arrays.asList(new StringEvent("Foo1"), new StringEvent("Foo2"), new StringEvent("Foo3")));
  }

  @Test
  public void processesEventsSynchronously() throws Exception {
    processor.start();
    processor.postEventAsync(new StringEvent("Foo1"));
    processor.postEventSync(new StringEvent("Foo2"));
    assertEquals(
        processor.processedEvents, Arrays.asList(new StringEvent("Foo1"), new StringEvent("Foo2")));
    processor.postEventAsync(new StringEvent("Foo3"));
    processor.interrupt();
    processor.waitForShutdown();
    assertEquals(
        processor.processedEvents,
        Arrays.asList(new StringEvent("Foo1"), new StringEvent("Foo2"), new StringEvent("Foo3")));
  }

  @Test
  public void listenersReceiveEvents() throws Exception {
    AddsToCollectionEventListener listener = new AddsToCollectionEventListener();
    processor.addListener(listener);
    processor.start();
    processor.postEventAsync(new StringEvent("Foo1"));
    processor.postEventAsync(new StringEvent("Foo2"));
    processor.postEventAsync(new StringEvent("Foo3"));
    processor.interrupt();
    processor.waitForShutdown();
    assertEquals(
        listener.willProcessEvents,
        Arrays.asList(new StringEvent("Foo1"), new StringEvent("Foo2"), new StringEvent("Foo3")));
    assertEquals(
        listener.processedEvents,
        Arrays.asList(new StringEvent("Foo1"), new StringEvent("Foo2"), new StringEvent("Foo3")));
  }

  private static final class StringEvent implements Event {
    private final String value;

    private StringEvent(String value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof StringEvent && ((StringEvent) other).value.equals(value);
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }

    @Override
    public String toString() {
      return value;
    }
  }

  private static final class AddsToCollectionEventListener implements EventListener<StringEvent> {
    private final List<StringEvent> willProcessEvents =
        Collections.synchronizedList(new ArrayList<>());
    private final List<StringEvent> processedEvents =
        Collections.synchronizedList(new ArrayList<>());

    @Override
    public void willProcessEvent(StringEvent event) {
      willProcessEvents.add(event);
    }

    @Override
    public void eventProcessed(StringEvent event) {
      processedEvents.add(event);
    }
  }

  private static final class AddsToCollectionEventProcessor
      extends EventProcessor<StringEvent, AddsToCollectionEventListener> {
    private final List<StringEvent> processedEvents =
        Collections.synchronizedList(new ArrayList<>());
    private boolean interrupted = false;

    @Override
    protected void process(StringEvent event) {
      processedEvents.add(event);
    }

    @Override
    protected void threadInterrupted(InterruptedException exception) {
      interrupted = true;
    }
  }
}
