package common.dataStructures.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import common.dataStructures.BiMap;

public abstract class ViewSet<E> extends AbstractSet<E> {

	private boolean isCollection;
	private Object source; //Either a collection or a map
	
	public ViewSet(Collection<?> c){
		isCollection = true;
		source = c;
	}
	
	public ViewSet(Map<?,?> m){
		isCollection = false;
		source = m;
	}
	
	public ViewSet(BiMap<?,?> m){
		isCollection = false;
		source = m;
	}
	
	@Override
	public final boolean add(E e){
		throw new UnsupportedOperationException("Can't add an element to a ViewSet");
	}
	
	@Override 
	public abstract boolean remove(Object o);

	@Override
	public abstract Iterator<E> iterator();
	
	@Override
	public void clear(){
		if(isCollection)
			((Collection<?>)source).clear();
		else
			((Map<?,?>)source).clear();
	}

	@Override
	public int size() {
		if(isCollection)
			return ((Collection<?>)source).size();
		else
			return ((Map<?,?>)source).size();
	}
	
	
}