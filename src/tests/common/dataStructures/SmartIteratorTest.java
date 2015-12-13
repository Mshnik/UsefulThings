package common.dataStructures;

import static common.JUnitUtil.*;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.junit.Test;

import common.dataStructures.util.SmartIterator;

public class SmartIteratorTest {

  @Test
  public void testSimpleUse() {
    List<Integer> list = Arrays.asList(0, 1, 2, 3, 4, 5);
    SmartIterator<Integer> sIter = new SmartIterator<Integer>(list, () -> 0);

    int i = 0;
    while (sIter.hasNext()) {
      assertEquals(i++, sIter.next().intValue());
    }
  }

  @Test
  public void testThrowsConcurrentModificationException() {
    List<Integer> list = new DeArrList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
    SmartIterator<Integer> sIter = (SmartIterator<Integer>) list.iterator();

    shouldFail(() -> {
      list.add(-1);
      sIter.hasNext();
    }, ConcurrentModificationException.class);

    shouldFail(() -> {
      list.remove(0);
      sIter.hasNext();
    }, ConcurrentModificationException.class);

    shouldFail(() -> {
      list.clear();
      sIter.hasNext();
    }, ConcurrentModificationException.class);
  }

  @Test
  public void testRemove() {
    List<Integer> list = new DeArrList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
    SmartIterator<Integer> sIter = (SmartIterator<Integer>) list.iterator();

    while (sIter.hasNext()) {
      sIter.next();
      sIter.remove();
    }

    assertTrue(list.isEmpty());

    List<Integer> list2 = new DeArrList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
    SmartIterator<Integer> sIter2 = (SmartIterator<Integer>) list2.iterator();
    sIter2.next();
    sIter2.remove();

    shouldFail(() -> {
      list2.add(12);
      sIter2.hasNext();
    }, ConcurrentModificationException.class);

  }

}
