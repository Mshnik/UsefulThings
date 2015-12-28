package common.dataStructures;

import java.util.Iterator;

import functional.impl.Function1;

//TODO - TEST
/**
 * A generic Trie, able to store any T that is Iterable of C
 *
 * @param <T> - the type of elements to store in this Trie
 * @param <C> - the type of the pieces that each T breaks down to.
 * @author Mshnik
 */
public class ITrie<T extends Iterable<C>, C> extends AbsTrie<T, C> {

  private final Function1<Iterable<C>, T> fromSequenceConverter;

  public ITrie(Function1<Iterable<C>, T> fromSequenceConverter) {
    this.fromSequenceConverter = fromSequenceConverter;
  }

  @Override
  protected Iterator<C> toSequence(T t) {
    return t.iterator();
  }

  @Override
  protected T fromSequence(Iterable<C> iter) {
    return fromSequenceConverter.apply(iter);
  }

}
