package concurrent;

public interface SynchroBuffer {

  public void waitUntilReady(String arg) throws InterruptedException;

}
