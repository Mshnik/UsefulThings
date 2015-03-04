package common.cons;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class ConsList<E> implements List<E>{
	
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
		
		return 
				 size == lst.size &&
				 (val == null && lst.val  == null || val  != null &&  val.equals(lst.val)) &&
				(tail == null && lst.tail == null || tail != null && tail.equals(lst.tail));
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		return val != null && val.equals(o) || tail != null && tail.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException("Can't add to consList, can only cons");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Can't remove from consList, they are immutable");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c){
			if(! contains(o)) return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException("Can't add to consList, can only cons");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("Can't add to consList, can only cons");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Can't remove from consList, they are immutable");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Can't remove from consList, they are immutable");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Can't remove from consList, they are immutable");		
	}

	@Override
	public E get(int index) {
		if(index < 0 || index >= size)
			throw new IllegalArgumentException("Can't get element at index " + index + " OOB");
		
		if(index == 0) return val;
		return tail.get(index - 1);
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException("Can't set in consList, they are immutable");		
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException("Can't add to consList, can only cons");
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException("Can't remove from consList, they are immutable");
	}

	@Override
	public int indexOf(Object o) {
		return indexOf(o, 0);
	}
	
	private int indexOf(Object o, int x){
		if (Objects.equals(val, o)) return x;
		else return 
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
