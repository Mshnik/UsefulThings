package common.dataStructures;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class StressTest {

  private static final int BIG_VAL = 200000;
  private static final float TOLERANCE = 1.1f;

  @Test
  public void testArrayListsPrepend() {
    ArrayList<Integer> arr1 = new ArrayList<Integer>();
    DeArrList<Integer> arr2 = new DeArrList<Integer>();

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < BIG_VAL; i++) {
      arr1.add(0, i);
      assertEquals(arr1.get(0).intValue(), i);
    }
    assertEquals(arr1.size(), BIG_VAL);
    long arr1Time = System.currentTimeMillis() - startTime;
    System.out.println("Standard Implementation Prepend " + arr1Time + "ms");

    startTime = System.currentTimeMillis();
    for (int i = 0; i < BIG_VAL; i++) {
      arr2.add(0, i);
      assertEquals(arr2.get(0).intValue(), i);
    }
    assertEquals(arr2.size(), BIG_VAL);
    long arr2Time = System.currentTimeMillis() - startTime;
    System.out.println("My Implementation Prepend " + arr2Time + "ms");

    assertTrue(arr2Time < arr1Time * TOLERANCE);
  }

  @Test
  public void testArrayListsAdd() {
    ArrayList<Integer> arr1 = new ArrayList<Integer>();
    DeArrList<Integer> arr2 = new DeArrList<Integer>();

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < BIG_VAL; i++) {
      int index = (int) (Math.random() * (arr1.size() + 1));
      arr1.add(index, i);
      assertEquals(i, arr1.get(index).intValue());
    }
    assertEquals(arr1.size(), BIG_VAL);
    long arr1Time = System.currentTimeMillis() - startTime;
    System.out.println("Standard Implementation Add " + arr1Time + "ms");

    startTime = System.currentTimeMillis();
    for (int i = 0; i < BIG_VAL; i++) {
      int index = (int) (Math.random() * (arr2.size() + 1));
      arr2.add(index, i);
      assertEquals(i, arr2.get(index).intValue());
    }
    assertEquals(arr2.size(), BIG_VAL);
    long arr2Time = System.currentTimeMillis() - startTime;
    System.out.println("My Implementation Add " + arr2Time + "ms");

    assertTrue(arr2Time < arr1Time * TOLERANCE);
  }

  @Test
  public void testRemove() {
    ArrayList<Integer> arr1 = new ArrayList<Integer>();
    DeArrList<Integer> arr2 = new DeArrList<Integer>();

    for (int i = 0; i < BIG_VAL; i++) {
      arr1.add(0, i);
      arr2.add(0, i);
    }

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < BIG_VAL; i++) {
      arr1.remove((int) (Math.random() * (arr1.size())));
    }
    assertEquals(0, arr1.size());
    long arr1Time = System.currentTimeMillis() - startTime;
    System.out.println("Standard Implementation Remove " + arr1Time + "ms");

    startTime = System.currentTimeMillis();
    for (int i = 0; i < BIG_VAL; i++) {
      arr2.remove((int) (Math.random() * (arr2.size())));
    }
    assertEquals(0, arr2.size());
    long arr2Time = System.currentTimeMillis() - startTime;
    System.out.println("My Implementation Remove " + arr2Time + "ms");

    assertTrue(arr2Time < arr1Time * TOLERANCE);
  }

}
