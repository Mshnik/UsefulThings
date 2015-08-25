package common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IDObjectTest {

  @Before
  public void setUp() throws Exception {
    IDObject.next_id.set(0);
  }

  @Test
  public void testID() {

    //Test that ids start at 0 and are populated in ascending order
    for (int i = 0; i < 10; i++) {
      IDObject x = new IDObject();
      assertEquals(x.getID(), i);
      assertEquals(IDObject.getNextID(), i + 1);
    }

    //Test that custom ID functions work and don't alter nextID
    for (int i = 0; i < 20; i++) {
      IDObject x = new IDObject(i);
      assertEquals(x.getID(), i++);
      assertEquals(IDObject.getNextID(), 10); //next shouldn't have changed
    }
  }

  @Test
  public void testEqualityAndHashcode() {
    ArrayList<IDObject> arr = new ArrayList<>();
    ArrayList<IDObject> copArr = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      arr.add(new IDObject());
      copArr.add(new IDObject(i)); //Doesn't advance next id
    }

    for (int i = 0; i < arr.size(); i++) {
      assertTrue(arr.get(i).equals(arr.get(i)));
      assertTrue(arr.get(i).equals(copArr.get(i)));

      assertEquals(arr.get(i).hashCode(), copArr.get(i).hashCode());

      assertFalse(arr.get(i).equals(null));
      assertFalse(arr.get(i).equals(arr.get((i + 1) % arr.size())));
    }
  }

  private static class IDMaker extends Thread {
    private List<IDObject> lst;
    private boolean done;
    private static final int NUMB_ITERATIONS = 10;

    @Override
    public void run() {
      done = false;
      for (int i = 0; i < NUMB_ITERATIONS; i++) {
        IDObject x = new IDObject();
        try {
          Thread.sleep((long) (Math.random() * 10));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        lst.add(x);
      }
      synchronized (this) {
        done = true;
        notify(); //wake up main
      }
    }
  }

  @Test
  public void testMultithreadedUse() throws InterruptedException {
    List<IDObject> objs = Collections.synchronizedList(new ArrayList<>());
    List<IDMaker> makers = new ArrayList<IDMaker>();

    //Should create ids in range [0..99] (10 threads, each make 10 and add)
    for (int i = 0; i < 10; i++) {
      IDMaker m = new IDMaker();
      makers.add(m);
      m.lst = objs;
      m.start();
    }

    //Do nothing until all the threads are done with work
    boolean done = false;
    while (!done) {
      done = true;
      for (IDMaker m : makers) {
        synchronized (m) {
          if (!m.done) {
            done = false;
            m.wait();
          }
        }
      }
    }

    assertEquals(100, objs.size());
    Collections.sort(objs, new Comparator<IDObject>() {
      @Override
      public int compare(IDObject o1, IDObject o2) {
        return o1.getID() - o2.getID();
      }
    });

    for (int i = 0; i < 100; i++) {
      assertEquals(objs.get(i).getID(), i);
    }
  }

}
