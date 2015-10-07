package common.dataStructures;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import common.Util;

/**
 * A Trie class capable of storing lists or arrays of E
 *
 * @param <E> - the elements within the arrays stored in this ArrTrie.
 * @author Mshnik
 */
public class ArrTrie<E> extends AbsTrie<List<E>, E> {

  @Override
  protected Iterator<E> toSequence(List<E> t) {
    return t.iterator();
  }

  @Override
  protected List<E> fromSequence(Iterable<E> iter) {
    return Collections.unmodifiableList(Util.toList(iter));
  }

  /**
   * See: {@link common.dataStructures.AbsTrie#add(Object listOfE)}
   */
  public boolean add(E[] arr) {
    return super.add(Arrays.asList(arr));
  }

  /**
   * See: {@link common.dataStructures.AbsTrie#contains(Object listOfE)}
   */
  public boolean contains(E[] arr) {
    return super.contains(Arrays.asList(arr));
  }

  /**
   * See: {@link common.dataStructures.AbsTrie#remove(Object listOfE)}
   */
  public boolean remove(E[] arr) {
    return super.remove(Arrays.asList(arr));
  }
}
