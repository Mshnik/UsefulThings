package common.dataStructures;

import java.util.AbstractList;
import java.util.Collection;

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
public class DeArrList<E> extends AbstractList<E> implements Cloneable{

	//start will never equal end. ReArray whenever this would become the case.
	private int start; //inclusive
	private int end; //exclusive
	private int size; //Number of elements in list
	private Object[] vals;

	private static final int DEFAULT_SIZE = 16;

	public DeArrList(){
		this(DEFAULT_SIZE);
	}

	public DeArrList(Collection<? extends E> c){
		this(DEFAULT_SIZE);
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

	//	/** Moves all elements starting at fake-index start down, 
	//	 * stopping after editing end. Goes by inc each time - 
	//	 * +1 for shifting right, -1 for shifting left.
	//	 * (fake index starts at 0, goes to size).
	//	 * Returns true if a reArray operation occured because of this */
	//	private boolean shift(int start, int end, int inc){
	//		boolean incSize = reArray();
	//		E temp = null;
	//		boolean shifted = false;
	//		boolean goingRight = (inc > 0);
	//		for(int i = start; (goingRight && i < end) || (! goingRight && i > end); i += inc){
	//			E here = null; 
	//			try{
	//				here = get(i);
	//				set(i, temp);
	//			}catch(ArrayIndexOutOfBoundsException e){}
	//			temp = here;
	//			shifted = true;
	//		}
	//		if(shifted) 		modCount++;
	//		return incSize;
	//	}

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
			if(end == vals.length ||
					(start < end && index < size()/2 || start > end && start < realIndex)){
				System.arraycopy(vals, start, vals, start-1, index);
				start = Util.mod((start-1),vals.length);
				vals[Util.mod(realIndex-1,vals.length)] = element;
			}
			//Shift right otherwise
			else {
				System.arraycopy(vals, realIndex, vals, realIndex+1, (end - realIndex));
				end = Util.mod((end+1),vals.length);
				vals[Util.mod(realIndex,vals.length)] = element;
			}
		}
		size++;
	}

	public void append(E e){
		add(size(), e);
	}

	public void prepend(E e){
		add(0, e);
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
		if(index >= size()/2){
			//shift(size() - 1, index-1, -1);
			vals[end - 1] = null;
			end = Util.mod((end-1),vals.length);
		}
		else{
			//shift(0, index + 1, 1);
			vals[start] = null;
			start = Util.mod((start+1),vals.length);
		}
		size--;
		return e;
	}

}
