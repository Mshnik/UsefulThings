package common.dataStructures;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import common.Util;

public class ArrTrie<E> extends AbsTrie<List<E>, E> {

	@Override
	protected Iterator<E> toSequence(List<E> t) {
		return t.iterator();
	}

	@Override
	protected List<E> fromSequence(Iterator<E> iter) {
		return Collections.unmodifiableList(Util.toList(iter));
	}
	
	public boolean add(E[] arr) {
		return super.add(Arrays.asList(arr));
	}
	
	public boolean contains(E[] arr){
		return super.contains(Arrays.asList(arr));
	}
	
	public boolean remove(E[] arr){
		return super.remove(Arrays.asList(arr));
	}
}
