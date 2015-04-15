package common.dataStructures.util;

import java.util.Map.Entry;

import common.types.Tuple2;

public class UnmodifiableEntry<K,V> extends Tuple2<K,V> implements Entry<K,V> {
	
	public UnmodifiableEntry(K k, V v){
		super(k,v);
	}
	
	@Override
	public K getKey() {
		return _1;
	}

	@Override
	public V getValue() {
		return _2;
	}

	@Override
	public V setValue(V value) {
		throw new UnsupportedOperationException("Can't set the value of an UnmodifiableEntry");
	}

}
