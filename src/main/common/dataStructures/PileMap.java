//package common.dataStructures;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
///**
// * @author Mshnik
// */
//public class PileMap {
//
//  private static class PileRange {
//    private final int startIndex; //Inclusive
//    private final int stopIndex; //Inclusive
//    private int value; //Mutable;
//
//    private PileRange(int startIndex, int size) {
//      this.startIndex = startIndex;
//      this.stopIndex = startIndex + size;
//      this.value = 0;
//
//      assert size > 0;
//      assert (size & (size - 1)) == 0; // Check size is power of 2
//    }
//
//    public int getStartIndex() {
//      return startIndex;
//    }
//
//    private void add(int delta) {
//      this.value += delta;
//    }
//
//    private int getValue() {
//      return this.value;
//    }
//
//    public String toString() {
//      return "[" + startIndex + "-" + stopIndex + "]:" + value;
//    }
//  }
//
//  /** Size of the PileMap. Big-O will be in terms of this value for PileMap. */
//  private final int originalSize;
//
//  /** Result of rounding originalSize up to the nearest power of 2. */
//  private final int size;
//
//  /** Log base 2 of size */
//  private final int logSize;
//
//  /**
//   * Lists of piles. All piles will have a range of a power of 2.
//   * A pile with range [x,y] will be at index [x, y/(y-x)].
//   * Total size = sum of i=0...n-1 of n/2**i = n + n/2 + n/4 .... = 2n.
//   * Thus total size is bounded by O(n).
//   *
//   * We can assume O(1) get access over this nested list. (Backed by ArrayLists).
//   */
//  private List<Map<Integer, PileRange>> piles;
//
//  public PileMap(int originalSize) {
//    this.originalSize = originalSize;
//    logSize = (int)Math.ceil(Math.log(originalSize)/Math.log(2));
//    // For simplicity, round up to next power of 2. Doesn't change O(n) calculations, and
//    // removes annoying corner cases. Can remove this later to not use unnecessary space.
//    size = (int)Math.pow(2, logSize);
//
//    //Init piles - total number of ranges = 2n.
//    piles = IntStream.range(0, logSize)
//        .map(x -> 1 << x)
//        .mapToObj(this::createRangesOfSize)
//        .collect(Collectors.toList());
//
//    //Total init time is O(n).
//  }
//
//  private Map<Integer, PileRange> createRangesOfSize(final int rangeSize) {
//    return IntStream
//        .range(0, this.size/rangeSize)
//        .map(x -> x*rangeSize)
//        .mapToObj(i -> new PileRange(i, rangeSize))
//        .collect(Collectors.toMap(PileRange::getStartIndex, Function.identity()));
//  }
//
//  /** Returns the value at position index. */
//  public int get(int index) {
//    int value = 0;
//    for(int i = 0; i < logSize; i++) {
//      value += piles.get(i).get(index / (1 << i)).getValue();
//    }
//    return value;
//  }
//
//  /** Adds val to all values in the (inclusive) range startIndex,stopIndex.
//   *  Assumes startIndex <= stopIndex.
//   */
//  public void add(int startIndex, int stopIndex, int val) {
//    addHelper(startIndex, stopIndex, val, 0, size, logSize-1);
//  }
//
//  private void addHelper(int startIndex, int stopIndex, int val, int rangeStart, int rangeStop, int rangeLevel) {
//    // Check if we are on exactly the right range. If so, apply the value and terminate.
//    if (startIndex == rangeStart && stopIndex == rangeStop) {
//      piles.get(rangeLevel).get(startIndex).value += val;
//      return;
//    }
//    // Calculate range overlap.
//    int overlapSize;
//    if (startIndex <= rangeStart && rangeStop <= stopIndex) {
//      overlapSize = rangeStop - rangeStart;
//    } else if (rangeStart <= startIndex && stopIndex <= rangeStop) {
//      overlapSize = stopIndex - startIndex;
//    } else if (stopIndex <= rangeStart) {
//      overlapSize = rangeStart - stopIndex;
//    } else { // Implies rangeStop <= startIndex, or ranges don't overlap.
//      overlapSize = startIndex - rangeStop;
//    }
//    if (overlapSize <= 0) {
//      //Nothing to do here.
//      return;
//    }
//    int rangeSize = rangeStop - rangeStart;
//
//    // Check if the range makes up at least half of the range size, if so apply and invert before continuing.
//    if (overlapSize >= (rangeStop - rangeStart)/2) {
//      piles.get(rangeLevel).get(startIndex).value += val;
//      addHelper(startIndex, rangeStart, -val, rangeStart, rangeLevel+1);
//    }
//  }
//
//  public static void main(String[] args) {
//    new PileMap(16);
//  }
//}
