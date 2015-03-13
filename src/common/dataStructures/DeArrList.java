package common.dataStructures;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import common.Util;

/** A DeArrList is a Double-Ended Array List.
 * By allowing the array to wrap around the underlying array in either direction,
 * it does not shift elements when adding to the beginning or the end of the array.
 * This allows it to achieve amortized O(1) append (add to end) and prepend (add to front)
 * behavior. Both operations are amortized O(1), as they do require 
 * reArraying the whole DeArrList whenever it would become full. <br>
 * Any shifting that is done as the result of an insertion (insert in middle)
 * is optimized to be the smaller of the two shifts. This does not change the 
 * O(N) time required to add or remove from the middle of the array, but
 * does speed up the functions in real time.
 * <br><br>
 * I came up with the idea while explaining to the section I teach about the
 * complexity differences between an ArrayList and a LinkedList.
 * One of the main points in the LinkedList's favor is O(1) prepend.
 * By contrast, the standard ArrayList implementation has to shift all elements
 * down 1 in order to insert an element at the front of the list. However,
 * both do have O(1) append. I realized that there's no reason I couldn't extend
 * the way ArrayList is able to do O(1) append to include O(1) prepend by
 * wrapping values around either side of the array.
 * @author Mshnik
 */
public class DeArrList<E> extends AbstractList<E> implements Cloneable, Deque<E>{

	//start will never equal end. ReArray whenever this would become the case.
	private int start; //inclusive
	private int end; //exclusive
	private int size; //Number of elements in list
	private Object[] vals;

	static final int DEFAULT_SIZE = 16;

	public DeArrList(){
		this(DEFAULT_SIZE);
	}

	public DeArrList(Collection<? extends E> c){
		this(Math.max(DEFAULT_SIZE, c.size()));
		addAll(c);
	}

	public DeArrList(int size){
		vals = new Object[size];
		start = size/4;
		end = size/4;
	}

	public DeArrList<E> clone(){
		return new DeArrList<E>(this);
	}

	@Override
	public int size(){
		return size;
	}
	
	int getStart(){
		return start;
	}
	
	int getEnd(){
		return end;
	}

	int getArrLength(){
		return vals.length;
	}
	
	/** Rotates the DeArrList such that the first element is at true position newStart.
	 * This is purely an internal operation and doesn't affect the list this represents
	 * from the outside */
	void rotateTo(int newStart){
		if(newStart < 0 || newStart >= vals.length)
			throw new ArrayIndexOutOfBoundsException();

		if(newStart == start) return;

		Object[] arr = new Object[vals.length];

		//Move main portion
		int portion1 = Math.min(size, Math.min(vals.length - start, vals.length - newStart));
		System.arraycopy(vals, start, arr, newStart, portion1);
		//Move bridge portion
		int portion2 = Math.min(size - portion1, Math.abs(start - newStart));
		if(portion2 > 0){
			System.arraycopy(vals, Util.mod(start + portion1, vals.length), arr, 
					Util.mod(newStart + portion1, arr.length), portion2);
			//Move final portion
			int portion3 = size - portion1 - portion2;
			if(portion3 > 0){
				System.arraycopy(vals, Util.mod(start + portion1 + portion2, vals.length), arr, 
						Util.mod(newStart + portion1 + portion2, arr.length), portion3);
			}
		}

		start = newStart;
		end = Util.mod(newStart + size,arr.length);
		vals = arr;
	}
	
	@Override
	public String toString(){
		if(size() == 0)
			return "()";
		String s = "(";
		for(E e : this){
			s += e + ",";
		}
		return s.substring(0, s.length() - 1) + ")";
	}
	
	/** Moves vals to a new array of double the length, starting at length / 4. */
	private boolean reArray(){
		if(size() >= vals.length){
			Object[] oArr = new Object[vals.length * 2];
			if(start < end){
				System.arraycopy(vals, start, oArr, vals.length/2, end - start);
			} else{
				System.arraycopy(vals, start, oArr, vals.length/2, vals.length - start);
				System.arraycopy(vals, 0, oArr, vals.length/2 + (vals.length - start), end);
			}
			start = vals.length/2;
			end = vals.length * 3 / 2;
			vals = oArr;
			return true;
		}
		return false;
	}

	@Override
	public void add(int index, E element) {
		if(index < 0 || index > size())
			throw new ArrayIndexOutOfBoundsException();
		reArray();

		int realIndex = Util.mod(start + index, vals.length);

		//Check for simple append, prepend operations
		if(index == size()){
			end = Util.mod((end+1),vals.length);
			vals[Util.mod(realIndex,vals.length)] = element;
		} else if(index == 0){
			start = Util.mod((start-1),vals.length);
			vals[Util.mod(realIndex-1,vals.length)] = element;
		} else {
			//Shift left if there is no room to shift right (end == vals.length) and 
			// (nondisjoint and in first half or disjoint and in second half
			if(end == vals.length || start != 0 &&
					(start < end && index < size()/2 || start > end && start < realIndex)){
				System.arraycopy(vals, start, vals, start-1, index);
				start = Util.mod((start-1),vals.length);
				vals[Util.mod(realIndex-1,vals.length)] = element;
			}
			//Shift right otherwise
			else {
				System.arraycopy(vals, realIndex, vals, realIndex+1, (size() - index));
				end = Util.mod((end+1),vals.length);
				vals[Util.mod(realIndex,vals.length)] = element;
			}
		}
		size++;
	}
	
	@Override
	public void push(E e) {
		add(0, e);		
	}
	
	@Override
	public boolean offer(E e) {
		add(e);
		return true;
	}
	
	@Override
	public void addFirst(E e) {
		add(0, e);
	}

	@Override
	public void addLast(E e) {
		add(e);
	}

	@Override
	public boolean offerFirst(E e) {
		add(0,e);
		return true;
	}

	@Override
	public boolean offerLast(E e) {
		return add(e);
	}

	@Override
	public E get(int index){
		if(index < 0 || index >= size())
			throw new ArrayIndexOutOfBoundsException();

		@SuppressWarnings("unchecked")
		E e = (E)vals[Util.mod((index + start),vals.length)];
		return e;
	}
	
	@Override
	public E element() {
		return get(0);
	}
	
	@Override
	public E getFirst() {
		return get(0);
	}

	@Override
	public E getLast() {
		return get(size() - 1);
	}

	@Override
	public E peek() {
		return peekFirst();
	}
	
	@Override
	public E peekFirst() {
		try{
			return get(0);
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public E peekLast() {
		try{
			return get(size() - 1);
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public E set(int index, E element) {
		if(index < 0 || index >= size())
			throw new ArrayIndexOutOfBoundsException();

		E prev = get(index);
		vals[Util.mod((index + start),vals.length)] = element;
		return prev;
	}

	@Override
	public E remove(int index) {
		E e = get(index);
		if(index == size() - 1){
			vals[Util.mod(end - 1, vals.length)] = null;
			end = Util.mod((end-1),vals.length);
		} else if(index == 0){
			vals[start] = null;
			start = Util.mod((start+1),vals.length);
		} else {
			int realIndex = Util.mod(start + index, vals.length);
			//Shift right if
			// (nondisjoint and in first half or disjoint and in second half
			if((start < end && index < size()/2 || start >= end && start < realIndex)){
				System.arraycopy(vals, start, vals, start+1, realIndex - start);
				vals[start] = null;
				start = Util.mod((start+1),vals.length);
			}
			//Shift left otherwise
			else {
				System.arraycopy(vals, realIndex + 1, vals, realIndex, end - realIndex - 1);
				vals[end - 1] = null;
				end = Util.mod((end-1),vals.length);
			}
		}
		size--;
		return e;
	}

	@Override
	public E poll() {
		try{
			return remove(0);
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	@Override
	public E pop() {
		return poll();
	}
	
	@Override
	public E remove() {
		return removeFirst();
	}
	
	@Override
	public E removeFirst() {
		return remove(0);
	}

	@Override
	public E removeLast() {
		return remove(size() - 1);
	}

	@Override
	public E pollFirst() {
		try{
			return remove(0);
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public E pollLast() {
		try{
			return remove(size() - 1);
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		try{
			return remove(indexOf(o)) != null;
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		try{
			return remove(lastIndexOf(o)) != null;
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}

	@Override
	public Iterator<E> descendingIterator() {
		throw new UnsupportedOperationException("Not Yet Implemented");
	}
}
