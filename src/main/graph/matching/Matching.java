package graph.matching;

import java.util.*;

import common.dataStructures.BiMap;
import common.types.*;

/**
 * Represents a Matching between objects of type A and type B.
 * Because objects are stored in sets and maps, Matchings do not
 * support duplicates of either A or B instances.
 * An A or B can only be matched to an object of the opposite type, and
 * can only be matched to at most one object at a time.
 * <br><br>
 * Matchings keep records of all valid matching objects internally.
 * This prevents unintentional addition of objects into the matching.
 * For use, this means that before any matching can be performed, all
 * matching partners should be added as valid by using the addA, addB,
 * and collection helper methods. From there the match(A, B) method can be
 * used to actually match objects.
 *
 * @param <A> - the type of the left side of the matching
 * @param <B> - the type of the right side of the matching
 * @author Mshnik
 */
public class Matching<A, B> {

  private HashSet<A> aObjects;
  private HashSet<B> bObjects;
  private BiMap<A, B> matching; //Can only contain pairs from aObjects and bObjects

  /**
   * Constructs a new empty matching
   */
  public Matching() {
    aObjects = new HashSet<>();
    bObjects = new HashSet<>();
    matching = new BiMap<>();
  }

  /**
   * Constructs a new matching with the given matching objects, but with
   * all unmatched
   *
   * @param aObjs - the initial A objects
   * @param bObjs - the initial B objects
   */
  public Matching(Set<? extends A> aObjs, Set<? extends B> bObjs) {
    this();
    addAllA(aObjs);
    addAllB(bObjs);
  }

  /**
   * Adds the given a to this Matching. Does not match it with anything.
   *
   * @return - true if a was not already in this Matching, false otherwise.
   */
  public boolean addA(A a) {
    if (aObjects.contains(a)) return false;
    aObjects.add(a);
    return true;
  }

  /**
   * Adds all of the given a's to this Matching. Does not match them with anything.
   *
   * @return - true if the matching was modified this way, false otherwise
   */
  public boolean addAllA(Collection<? extends A> colA) {
    boolean changed = false;
    for (A a : colA) {
      changed = addA(a) | changed;
    }
    return changed;
  }

  /**
   * Adds the given b to this Matching. Does not match it with anything.
   *
   * @return - true if b was not already in this Matching, false otherwise.
   */
  public boolean addB(B b) {
    if (bObjects.contains(b)) return false;
    bObjects.add(b);
    return true;
  }

  /**
   * Adds all of the given b's to this Matching. Does not match them with anything.
   *
   * @return - true if the matching was modified this way, false otherwise
   */
  public boolean addAllB(Collection<? extends B> colB) {
    boolean changed = false;
    for (B b : colB) {
      changed = addB(b) | changed;
    }
    return changed;
  }

  /**
   * Returns true if the object exists in this matching (matched or not)
   */
  public boolean contains(Object o) {
    return aObjects.contains(o) || bObjects.contains(o);
  }

  /**
   * Returns true iff o is currently matched in this matching
   */
  public boolean isMatched(Object o) {
    return matching.containsKey(o) || matching.containsValue(o);
  }

  /**
   * Returns true iff o is currently unmatched in this matching, but is
   * a valid object in this matching.
   */
  public boolean isUnmatched(Object o) {
    return (aObjects.contains(o) || bObjects.contains(o)) && !isMatched(o);
  }

  /**
   * Returns the A that the given b is matched to. Returns null if b is unmatched
   */
  public A getMatchedA(B b) {
    return matching.getKey(b);
  }

  /**
   * Returns the B that the given a is matched to. Returns null if a is unmatched
   */
  public B getMatchedB(A a) {
    return matching.getValue(a);
  }

  /**
   * Causes a and b to become matched. Does nothing if a or b are not valid matching
   * partners in this matching (must have been previously added to via addA and addB).
   * This will cause the old partners of a and b (if any) to become unmatched.
   *
   * @return - true if a and b are now matched, false if the operation was aborted.
   */
  public boolean match(A a, B b) {
    if (!aObjects.contains(a) || !bObjects.contains(b))
      return false;
    matching.put(a, b);
    return true;
  }

  /**
   * Returns a Map of As to their current matchings B. Unmatched elements
   * will not be included
   */
  public BiMap<A, B> getMatching() {
    return matching.clone();
  }

  /**
   * Returns a Map of Bs to their current matchings A. Unmatched elements
   * will not be included
   */
  public BiMap<B, A> getFlippedMatching() {
    return matching.flip();
  }

  /**
   * Returns a set of the A that are currently matched
   */
  public Set<A> getMatchedA() {
    HashSet<A> obj = new HashSet<>();
    for (A a : aObjects) {
      if (matching.containsKey(a)) {
        obj.add(a);
      }
    }
    return obj;
  }

  /**
   * Returns a set of the A that are currently matched
   */
  public Set<B> getMatchedB() {
    HashSet<B> obj = new HashSet<>();
    for (B b : bObjects) {
      if (matching.containsValue(b)) {
        obj.add(b);
      }
    }
    return obj;
  }

  /**
   * Returns a set of the A that are currently matched
   */
  public Set<A> getUnmatchedA() {
    HashSet<A> obj = new HashSet<>();
    for (A a : aObjects) {
      if (!matching.containsKey(a)) {
        obj.add(a);
      }
    }
    return obj;
  }

  /**
   * Returns a set of the A that are currently matched
   */
  public Set<B> getUnmatchedB() {
    HashSet<B> obj = new HashSet<>();
    for (B b : bObjects) {
      if (!matching.containsValue(b)) {
        obj.add(b);
      }
    }
    return obj;
  }

  /**
   * Returns a set of the elements that are currently matched.
   *
   * @return a set of Either{@literal <A,B>}, where each element
   * is a Left{@literal <A>} or a Right{@literal <B>}
   */
  public HashSet<Either<A, B>> getMatched() {
    HashSet<Either<A, B>> obj = new HashSet<>();
    for (A a : getMatchedA()) {
      obj.add(new Left<A, B>(a));
    }
    for (B b : getMatchedB()) {
      obj.add(new Right<A, B>(b));
    }
    return obj;
  }

  /**
   * Returns a set of the elements that are currently unmatched.
   *
   * @return a set of Either{@literal <A,B>}, where each element
   * is a Left{@literal <A>} or a Right{@literal <B>}
   */
  public HashSet<Either<A, B>> getUnmatched() {
    HashSet<Either<A, B>> obj = new HashSet<>();
    for (A a : getUnmatchedA()) {
      obj.add(new Left<A, B>(a));
    }
    for (B b : getUnmatchedB()) {
      obj.add(new Right<A, B>(b));
    }
    return obj;
  }

  /**
   * Returns true if o is a matching, and they have the same sets of
   * matching items, and the same actual matching. False otherwise
   */
  @Override
  public boolean equals(Object o) {
    try {
      Matching<?, ?> m = (Matching<?, ?>) o;
      return aObjects.equals(m.aObjects) &&
          bObjects.equals(m.bObjects) &&
          matching.equals(m.matching);
    } catch (ClassCastException e) {
      return false;
    }
  }

  /**
   * Hashes a Matching by its sets of matchings items, and the actual matching.
   */
  @Override
  public int hashCode() {
    return Objects.hash(aObjects, bObjects, matching);
  }

  /**
   * Returns a string representation of this matching, based on the current
   * matching and the set of unmatched items
   */
  @Override
  public String toString() {
    String unmatched = "[";

    for (Either<A, B> e : getUnmatched()) {
      unmatched += e.getVal().toString() + ", ";
    }

    unmatched = unmatched.substring(0, unmatched.length() - 2) + "]";

    return matching.toString() + ", Unmatched: " + unmatched;
  }
}
