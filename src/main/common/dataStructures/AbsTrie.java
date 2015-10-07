package common.dataStructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import common.dataStructures.util.SmartIterator;

/**
 * AbsTrie implements all of the Trie logic, leaving the type definitions
 * of the elements to the subclass.
 * <p>
 * A Trie is a compact tree-based data structure that stores elements that can be
 * broken down in to a list of pieces. For example, strings can be stored in a Trie
 * because they can be broken down into a list of characters. Most of the time complexities
 * of a Trie are based off of the length of the list when a T instance is broken down.
 * <br><br>
 * As a general rule (sir), tries are appropriate for use when the size of the dictionary
 * (number of possible instances of T) is very large, but the average length of a
 * T when converting to List of C is fairly short.
 * <br><br>
 * Ideally this could be a non-abstract class. However, there are three main categories
 * of objects that should be able to be stored in a Trie:
 * <ul>
 * <li> Any T which implements Iterable[C]
 * <li> Arrays of C
 * <li> Strings (with Characters as their pieces)
 * </ul>
 * Because Java's type system is... lacking, these three types are unrelated.
 * Thus the AbsTrie class handles all of the logic, and the three children simply
 * plug in the requisite types.
 *
 * @param <T> - the type of elements to store in this Trie. Must be convertible to Iterable(C).
 * @param <C> - the type of piece that each T instance breaks down to.
 * @author Mshnik
 */
abstract class AbsTrie<T, C> implements Set<T> {

  /**
   * The TrieNode that represents this AbsTrie. All of the storage in this Trie
   * starts at the root and goes through its children
   */
  private TrieNode root;

  /**
   * The number of T instances currently stored in this AbsTrie.
   */
  private int size;

  /**
   * The number of modifications made to this AbsTrie. Used to detect concurrent modification.
   */
  protected int modCount;

  /**
   * Returns the root node of this Trie. Exposed to package for testing purposes.
   */
  TrieNode getRoot() {
    return root;
  }

  /**
   * Constructs a new empty Trie
   */
  protected AbsTrie() {
    root = new TrieNode(null, false, null);
    size = 0;
  }

  /**
   * Converts an instance of t into its piece c's
   * This function should be the inverse of fromSequence.
   * Specifically, {@code t.equals(fromSequence(toSequence(t)))} for every
   * non-null t should be true
   *
   * @param t - the object to break down into pieces.
   * @return - the sequences of pieces that represents the t.
   */
  protected abstract Iterator<C> toSequence(T t);

  /**
   * Converts a sequence of c's into an instance of t.
   * This function should be the inverse of toSequence.
   * Specifically, {@code t.equals(fromSequence(toSequence(t)))} for every
   *
   * @param iter - an iterable representing the sequence of c's to convert
   * @return - an instance of T representing the whole sequence of c's
   */
  protected abstract T fromSequence(Iterable<C> iter);

  /**
   * Compares two instance of C for ordering.
   * If c1 and c2 are not comparable (C does not implement Comparable[C]), this will
   * throw an UnsupportedOperationException.
   * If the two do support ordering, the Comparable compareTo method will be used.
   * Overriding this function in subclasses allows the Trie to iterate in the order
   * of comparison given. As for the standard comparison contract, this should return:
   * {@code -1 if c1 < c2, 1 if c2 > c1, 0 otherwise}
   *
   * @param c1 - the first C to compare
   * @param c2 - the second C to compare
   */
  protected int compareC(C c1, C c2) throws UnsupportedOperationException {
    try {
      @SuppressWarnings("unchecked")
      Comparable<C> compC1 = (Comparable<C>) c1;
      return compC1.compareTo(c2);
    } catch (ClassCastException e) {
      throw new UnsupportedOperationException("Can't compare " + c1 + " and " + c2);
    }
  }

  public boolean contains(Object o) {
    try {
      @SuppressWarnings("unchecked")
      Iterator<C> iter = toSequence((T) o);
      TrieNode node = root;
      while (iter.hasNext()) {
        C c = iter.next();
        if (!node.children.containsKey(c)) {
          return false;
        }
        node = node.children.get(c);
      }
      return node.isTerminatingNode;
    } catch (ClassCastException c) {
      return false;
    }
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!contains(o)) return false;
    }
    return true;
  }

  public boolean add(T t) {
    Iterator<C> iter = toSequence(t);
    TrieNode node = root;
    while (iter.hasNext()) {
      C c = iter.next();
      if (!node.children.containsKey(c)) {
        node.children.put(c, new TrieNode(c, false, node));
      }
      node = node.children.get(c);
    }
    if (node.isTerminatingNode) return false;
    node.isTerminatingNode = true;
    size++;
    modCount++;
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean changed = false;
    for (T t : c) {
      changed = add(t) | changed;
    }
    return changed;
  }

  /**
   * Returns a list that contains the same elements (Ts) as this AbsTrie.
   *
   * @return - a list with size {@code size()}, containing the elements of this list.
   */
  public List<T> toList() {
    return root.buildList(new DeArrList<>(), new ConsList<>());
  }

  @Override
  public Iterator<T> iterator() {
    return new SmartIterator<T>(toList().iterator(), () -> modCount);
  }

  @Override
  public Object[] toArray() {
    Object[] arr = new Object[size()];
    int i = 0;
    for (T t : this) {
      arr[i++] = t;
    }
    return arr;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <X> X[] toArray(X[] a) {
    X[] arr = a.length >= size() ? a : Arrays.copyOf(a, size());
    int i = 0;
    for (T t : this) {
      arr[i++] = (X) t;
    }
    return arr;
  }

  public boolean remove(Object o) {
    try {
      @SuppressWarnings("unchecked")
      Iterator<C> iter = toSequence((T) o);
      TrieNode node = root;
      while (iter.hasNext()) {
        C c = iter.next();
        if (!node.children.containsKey(c)) {
          return false;
        }
        node = node.children.get(c);
      }
      if (!node.isTerminatingNode) return false;
      node.isTerminatingNode = false;

      //Remove now unused nodes
      while (node.children.isEmpty()) {
        TrieNode parent = node.parent;
        parent.children.remove(node.c);
        node = parent;
      }
      size--;
      modCount++;
      return true;
    } catch (ClassCastException c) {
      return false;
    }
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean changed = false;
    for (Object o : c) {
      changed = remove(o) | changed;
    }
    return changed;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean changed = false;
    List<T> contents = toList();
    for (T t : contents) {
      if (!c.contains(t)) {
        changed = remove(t) | changed;
      }
    }
    return changed;
  }

  @Override
  public void clear() {
    root.children.clear();
    size = 0;
    modCount++;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Represents a single node in the Trie.
   * Each node has a C value that is the value of that single node
   * and a boolean indicating if the path to this point is a valid element in the Trie.
   *
   * @author Mshnik
   */
  class TrieNode {

    /**
     * The value stored by this TrieNode
     */
    public final C c;

    /**
     * True if the path to this point is an element in the Trie, false otherwise
     */
    boolean isTerminatingNode;

    /**
     * The Trie nodes that are successors to this TrieNode
     */
    HashMap<C, TrieNode> children;

    /**
     * The Trie Node that is the parent of this node. Null if this is the root
     */
    TrieNode parent;

    /**
     * Constructs a new TrieNode. The names are pretty self explanatory.
     *
     * @param c                 - the value of this TrieNode
     * @param isTerminatingNode - true if this TrieNode is a terminating Node
     * @param parent            - the parent of this TrieNode.
     */
    TrieNode(C c, boolean isTerminatingNode, TrieNode parent) {
      this.c = c;
      this.isTerminatingNode = isTerminatingNode;
      children = new HashMap<>();
      this.parent = parent;
    }

    public String toString() {
      return c + "-(term=" + isTerminatingNode + ") " + children;
    }

    /**
     * Recursive helper method that builds all of the T elements
     * that this TrieNode represents.
     *
     * @param builderList - the list of Ts that have been built thus far.
     * @param prefix      - the C elements that should be prepended on to any elements built from here.
     * @return - builderList
     */
    List<T> buildList(List<T> builderList, ConsList<C> prefix) {
      if (c != null) {
        prefix = prefix.cons(c);
      }
      if (isTerminatingNode) {
        builderList.add(fromSequence(prefix.reverse()));
      }

      List<Entry<C, TrieNode>> lst = new DeArrList<>(children.entrySet());
      try {
        Collections.sort(lst, (c1, c2) -> compareC(c1.getKey(), c2.getKey()));
      } catch (UnsupportedOperationException e) {}

      for (Entry<C, TrieNode> entry : lst) {
        builderList = entry.getValue().buildList(builderList, prefix);
      }

      return builderList;
    }
  }
}
