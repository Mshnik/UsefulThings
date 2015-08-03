package common.dataStructures;

import java.util.Iterator;

import functional.Function;

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
