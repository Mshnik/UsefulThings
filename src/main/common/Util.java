package common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import common.dataStructures.DeArrList;

public class Util {

  /**
   * Returns a random element from the given iterable.
   * Useful for getting random element from non-indexed collections, like
   * HashSets.
   * <br><br>
   * Synchronizes on the given col to prevent concurrent modification.
   * Returns null if col is null or empty.
   */
  public static <E> E randomElement(Collection<E> col) {
    if (col == null)
      return null;

    Iterator<E> iterator;
    synchronized (col) {

      if (col.isEmpty())
        return null;

      iterator = col.iterator();
      int r = (int) (Math.random() * col.size());
      for (int i = 0; i < r; i++) {
        iterator.next();
      }
    }
    return iterator.next();
  }

  /**
   * Returns the MATHEMATICAL Mod of the two arguments, A % B.
   * FK java for having a nonsensical interpretation of the mod operator.
   */
  public static int mod(int a, int b) {
    return ((a % b) + b) % b;
  }

  /**
   * Returns a generic array containing objects of type T, with length length.
   * This is by definition unchecked.
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] createArray(Class<T> clazz, int length) {
    return (T[]) Array.newInstance(clazz, length);
  }

  public static <E> ArrIterator<E> arrIterator(E[] arr) {
    return new ArrIterator<E>(arr);
  }

  static class ArrIterator<E> implements Iterator<E> {
    private final E[] sourceArr;
    private int index;

    public ArrIterator(E[] source) {
      sourceArr = Arrays.copyOf(source, source.length);
      index = 0;
    }

    @Override
    public boolean hasNext() {
      return index < sourceArr.length;
    }

    @Override
    public E next() {
      return sourceArr[index++];
    }
  }

  public static <T> List<T> toList(Iterator<T> iter) {
    DeArrList<T> lst = new DeArrList<>();
    iter.forEachRemaining((a) -> lst.add(a));
    return lst;
  }

  /**
   * Returns an Short array that is equivalent to the given short array
   */
  public static Short[] boxArr(short[] arr) {
    Short[] arr2 = new Short[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i];
    }
    return arr2;
  }

  /**
   * Returns an Integer array that is equivalent to the given int array
   */
  public static Integer[] boxArr(int[] arr) {
    Integer[] arr2 = new Integer[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i];
    }
    return arr2;
  }

  /**
   * Returns an Long array that is equivalent to the given long array
   */
  public static Long[] boxArr(long[] arr) {
    Long[] arr2 = new Long[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i];
    }
    return arr2;
  }

  /**
   * Returns an Float array that is equivalent to the given float array
   */
  public static Float[] boxArr(float[] arr) {
    Float[] arr2 = new Float[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i];
    }
    return arr2;
  }

  /**
   * Returns an Double array that is equivalent to the given double array
   */
  public static Double[] boxArr(double[] arr) {
    Double[] arr2 = new Double[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i];
    }
    return arr2;
  }

  /**
   * Returns an Character array that is equivalent to the given char array
   */
  public static Character[] boxArr(char[] arr) {
    Character[] arr2 = new Character[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i];
    }
    return arr2;
  }

  /**
   * Returns an short array that is equivalent to the given Short array.
   * Any null values encountered are given the value 0
   */
  public static short[] unboxArr(Short[] arr) {
    short[] arr2 = new short[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i] == null ? 0 : arr[i];
    }
    return arr2;
  }

  /**
   * Returns an int array that is equivalent to the given Integer array.
   * Any null values encountered are given the value 0
   */
  public static int[] unboxArr(Integer[] arr) {
    int[] arr2 = new int[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i] == null ? 0 : arr[i];
    }
    return arr2;
  }

  /**
   * Returns an long array that is equivalent to the given Long array.
   * Any null values encountered are given the value 0
   */
  public static long[] unboxArr(Long[] arr) {
    long[] arr2 = new long[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i] == null ? 0L : arr[i];
    }
    return arr2;
  }

  /**
   * Returns an float array that is equivalent to the given Float array.
   * Any null values encountered are given the value 0.0
   */
  public static float[] unboxArr(Float[] arr) {
    float[] arr2 = new float[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i] == null ? 0.0f : arr[i];
    }
    return arr2;
  }

  /**
   * Returns an double array that is equivalent to the given Double array.
   * Any null values encountered are given the value 0.0
   */
  public static double[] unboxArr(Double[] arr) {
    double[] arr2 = new double[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i] == null ? 0.0 : arr[i];
    }
    return arr2;
  }

  /**
   * Returns an char array that is equivalent to the given Character array.
   * Any null values encountered are given the value 0
   */
  public static char[] unboxArr(Character[] arr) {
    char[] arr2 = new char[arr.length];
    for (int i = 0; i < arr.length; i++) {
      arr2[i] = arr[i] == null ? 0 : arr[i];
    }
    return arr2;
  }

  /**
   * Returns all permutations of the given array
   */
  public static <T> ArrayList<T[]> permute(T[] arr) {
    return permute(Arrays.copyOf(arr, arr.length), new ArrayList<T[]>(), 0);
  }

  /**
   * Helper function for permute. Recursively builds permutations of the given array
   *
   * @param arr   - the array to permute. Already permuted [0... index-1]
   * @param built - the permutations of the array that have already been built
   * @param index - the next index to permute over. When this is arr.length, recursion stops
   */
  private static <T> ArrayList<T[]> permute(T[] arr, ArrayList<T[]> built, int index) {
    if (index == arr.length) {
      built.add(Arrays.copyOf(arr, arr.length));
      return built;
    }

    for (int i = index; i < arr.length; i++) {
      //Swap i and index
      T temp = arr[i];
      arr[i] = arr[index];
      arr[index] = temp;

      permute(arr, built, index + 1);

      //Swap back
      temp = arr[i];
      arr[i] = arr[index];
      arr[index] = temp;
    }

    return built;
  }
}
