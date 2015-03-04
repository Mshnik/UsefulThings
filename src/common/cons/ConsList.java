package common.cons;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class ConsList<E> implements Iterable<E>{
	
	public static final String NIL_STRING = "_NIL_";
	
	public final ConsList<E> tail;
	public final E val;
	public final int size;

	public ConsList(){
		tail = null;
		val = null;
		size = 0;
	}

	private ConsList(E val, ConsList<E> tail, int size){
		this.tail = tail;
		this.val = val;
		this.size = size;
		if(val == null || tail == null || size < 1)
			throw new RuntimeException("Illegal ConsList construction" + this);
	}
	
	public E value(){
		return val;
	}
	
	public boolean isNil(){
		return tail == null && val == null;
	}
	
	public boolean isLast(){
		return isNil() || tail.tail == null && tail.val == null;
	}

	public ConsList<E> cons(E head){
		return new ConsList<E>(head, this, size + 1);
	}

	public String toString(){
		String s = "(";
		ConsList<E> current = this;
		while(current != null){
			if(current.val == null && current.tail == null){
				if(current == this) s += NIL_STRING + ",";
			} else {
				s += current.val + ",";	
			}
			current = current.tail;
		}
		return s.substring(0, s.length() - 1) + ")";
	}
	
	public boolean equals(Object o){
		if(! (o instanceof ConsList<?>) || o == null) return false;
		
		ConsList<?> lst = (ConsList<?>)o;
		
		return size == lst.size && Objects.equals(val, lst.val) && Objects.equals(tail, lst.tail);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean contains(Object o) {
		return val != null && val.equals(o) || tail != null && tail.contains(o);
	}

	public Iterator<E> iterator() {
		return new ConsIterator<E>(this);
	}

	public Object[] toArray() {
		Object[] arr = new Object[size];
		ConsList<E> current = this;
		for(int i = 0; i < size; i++, current = current.tail){
			arr[i] = current.val;
		}
		return arr;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] arr) {
		if(arr.length < size){
			arr = Arrays.copyOf(arr, size);
		}
		ConsList<E> current = this;
		for(int i = 0; i < size; i++, current = current.tail){
			arr[i] = (T) current.val;
		}
		return arr;
	}

	public boolean containsAll(Collection<?> c) {
		for(Object o : c){
			if(! contains(o)) return false;
		}
		return true;
	}

	public E get(int index) {
		if(index < 0 || index >= size)
			throw new IllegalArgumentException("Can't get element at index " + index + " OOB");
		
		if(index == 0) return val;
		return tail.get(index - 1);
	}

	public int indexOf(Object o) {
		return indexOf(o, 0);
	}
	
	private int indexOf(Object o, int x){
		if (Objects.equals(val, o)) return x;
		else if(isLast()) return -1;
		return tail.indexOf(0, x+1);
	}

	public static class ConsIterator<E> implements Iterator<E>{

		private ConsList<E> current;
		
		public ConsIterator(ConsList<E> first){
			current = first;
		}
		
		@Override
		public boolean hasNext() {
			return ! current.isLast();
		}

		@Override
		public E next() {
			E val = current.val;
			current = current.tail;
			return val;
		}
		
	}
	
}
