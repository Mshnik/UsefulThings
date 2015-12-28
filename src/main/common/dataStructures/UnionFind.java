package common.dataStructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//TODO - SPEC
public class UnionFind<E> {

  /**
   * A wrapper for an element in a UnionFind.
   * Maintains the value it wraps, the size of the union it is in,
   * and a parent pointer. If this is at the top of its union, the
   * parent pointer is itself.
   *
   * @author Mshnik
   */
  private static class Node<E> {
    private Node<E> parent;
    private E val;
    private int size;

    /**
     * Constructs a new node in its own union. Has size 1 and parent = this
     *
     * @param val - the value to wrap in this node
     */
    private Node(E val) {
      this.val = val;
      parent = this;
      size = 1;
    }

    /**
     * Returns a string representation of this Node, showing the chain up the parentage.
     */
    public String toString() {
      return parent == this ? val.toString() : val + "->" + parent.toString();
    }
  }

  /**
   * The elements (and their associated nodes) that are contained in this UnionFind
   */
  private HashMap<E, Node<E>> elms;

  /**
   * Constructs an empty UnionFind data structure
   */
  public UnionFind() {
    this.elms = new HashMap<E, Node<E>>();
  }

  /**
   * Constructs a UnionFind data structure initially containing the given
   * elements. Each element is placed in its own union (with size 1).
   */
  public UnionFind(Set<E> elms) {
    this();
    for (E e : elms) {
      add(e);
    }
  }

  /**
   * Adds the given element to this union find, if not already present.
   * If this addition is performed, places it in its own union with size 1.
   *
   * @param e - the element to add
   * @return - true if the UnionFind was altered as a result of this operation.
   */
  public boolean add(E e) {
    if (elms.containsKey(e))
      return false;
    elms.put(e, new Node<E>(e));
    return true;
  }

  /**
   * Returns a set of the elements contained in this UnionFind
   */
  public Set<E> toElmSet() {
    return new HashSet<E>(elms.keySet());
  }

  /**
   * Returns a set of sets of elements. Each subset is a union in the UnionFind
   */
  public Set<Set<E>> toUnionSet() {
    HashSet<Set<E>> h = new HashSet<>();
    for (E e : elms.keySet()) {

      boolean added = false;

      for (Set<E> s : h) {
        E setElm = s.iterator().next();
        if (isUnion(e, setElm)) {
          s.add(e);
          added = true;
          break;
        }
      }

      if (!added) {
        HashSet<E> s2 = new HashSet<E>();
        s2.add(e);
        h.add(s2);
      }
    }
    return h;
  }

  /**
   * Returns the element at the top of {@code elm}'s union. Runs in O(1) amortized
   * time because of compression applied after the operation. (O(logN) in worst case).
   *
   * @throws NotInCollectionException - if elm is not in this UnionFind
   */
  public E find(E elm) throws NotInCollectionException {
    if (!elms.containsKey(elm)) throw new NotInCollectionException("Can't find ", elm);
    return find(elms.get(elm));
  }

  /**
   * Helper function for find(E). Returns the element at the top of n's union
   */
  private E find(Node<E> n) {
    if (n.parent == n)
      return n.val;
    E val = find(n.parent);

    //Compress
    n.parent = elms.get(val);
    n.size = 1;

    return val;
  }

  /**
   * Returns the size of the union that elm is contained in. Runs in O(1)
   * amortized time. (O(logN) in worst case).
   *
   * @throws NotInCollectionException - if elm is not in this UnionFind
   */
  public int size(E elm) throws NotInCollectionException {
    if (!elms.containsKey(elm))
      throw new NotInCollectionException("Can't get size of ", elm);
    return elms.get(find(elm)).size;
  }

  /**
   * Unions the two elements. If they are already unioned, does not change anything
   *
   * @param elm1 - the first element to union
   * @param elm2 - the second element to union
   * @return - the element that is the parent of the union. If the two elements
   * were already unioned, returns elm1.
   * @throws NotInCollectionException - if elm1 or elm2 are not in the collection.
   */
  public E union(E elm1, E elm2) throws NotInCollectionException {
    if (!elms.containsKey(elm1) || !elms.containsKey(elm2))
      throw new NotInCollectionException("Can't union ", elm1, elm2);

    Node<E> p1 = elms.get(find(elm1)); //parent of elm1
    Node<E> p2 = elms.get(find(elm2)); //parent of elm2

    if (p1 == p2) return p1.val;

    if (p1.size < p2.size) {
      p1.parent = p2;
      p2.size += p1.size;
      return p2.val;
    } else {
      p2.parent = p1;
      p1.size += p2.size;
      return p1.val;
    }
  }

  /**
   * Returns true iff elm1 and elm2 are currently unioned in this UnionFind.
   *
   * @throws NotInCollectionException - if elm1 or elm2 are not in this UnionFind.
   */
  public boolean isUnion(E elm1, E elm2) throws NotInCollectionException {
    if (!elms.containsKey(elm1) || !elms.containsKey(elm2))
      throw new NotInCollectionException("Can't check union of ", elm1, elm2);

    return find(elm1).equals(find(elm2));
  }

}
