package common.dataStructures;

import java.util.Iterator;

import functional.Function;

/** A generic Trie, able to store any T that is Iterable of C
 * @author Mshnik
 *
 * @param <T> - the type of elements to store in this Trie
 * @param <C> - the type of the pieces that each T breaks down to.
 */
public class ITrie<T extends Iterable<C>, C> extends AbsTrie<T,C> {

	private final Function<Iterator<C>, T> fromSequenceConverter;
	
	public ITrie(Function<Iterator<C>, T> fromSequenceConverter) {
		this.fromSequenceConverter = fromSequenceConverter;
	}
	
	@Override
	protected Iterator<C> toSequence(T t) {
		return t.iterator();
	}

	@Override
	protected T fromSequence(Iterator<C> iter) {
		return fromSequenceConverter.apply(iter);
	}

}
