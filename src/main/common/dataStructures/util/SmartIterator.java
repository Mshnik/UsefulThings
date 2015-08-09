package common.dataStructures.util;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import functional.Supplier;

public class SmartIterator<T> implements Iterator<T> {

	private Iterator<T> wrappedIter;
	private int expectedModCount;
	private Supplier<Integer> modCountSupplier;
	
	public SmartIterator(Iterable<T> iterable, Supplier<Integer> getExpectedModCount){
		this(iterable.iterator(), getExpectedModCount);
	}
	
	public SmartIterator(Iterator<T> iter, Supplier<Integer> getExpectedModCount){
		wrappedIter = iter;
		modCountSupplier = getExpectedModCount;
		expectedModCount = getExpectedModCount.apply();
	}
	
	@Override
	public boolean hasNext() throws ConcurrentModificationException {
		if(expectedModCount != modCountSupplier.apply()) {
			throw new ConcurrentModificationException();
		}
		return wrappedIter.hasNext();
	}

	@Override
	public T next() {
		return wrappedIter.next();
	}
	
	@Override
	public void remove() {
		wrappedIter.remove();
		expectedModCount = modCountSupplier.apply();
	}

}
