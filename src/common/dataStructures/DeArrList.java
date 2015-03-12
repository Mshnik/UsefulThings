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
		if(end >= start) return end - start;
		else return end + vals.length - start;
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
		if(size() >= vals.length - 1){
			Object[] oArr = new Object[vals.length * 2];
			int i = oArr.length/4;
			for(E elm : this){
				oArr[i] = elm;
				i++;
			}

			vals = oArr;
			start = vals.length/4;
			end = i;
			return true;
		}
		return false;
	}

	/** Moves all elements starting at fake-index start down, 
	 * stopping after editing end. Goes by inc each time - 
	 * +1 for shifting right, -1 for shifting left.
	 * (fake index starts at 0, goes to size).
	 * Returns true if a reArray operation occured because of this */
	private boolean shift(int start, int end, int inc){
		boolean incSize = reArray();
		E temp = null;
		boolean goingRight = (inc > 0);
		for(int i = start; (goingRight && i < end) || (! goingRight && i > end); i += inc){
			E here = get(i);
			set(i, temp);
			temp = here;
		}
		return incSize;
	}

	@Override
	public void add(int index, E element) {
		if(index < -1 || index > size())
			throw new ArrayIndexOutOfBoundsException();
		reArray();
		if(index >= size()/2) {
			end = Util.mod((end+1),vals.length);
			shift(index, size(), 1);
			vals[Util.mod(start + index,vals.length)] = element;
		}
		else {
			start = Util.mod((start-1),vals.length);
			shift(index, -1, -1);
			vals[Util.mod(start + index,vals.length)] = element;
		}
		modCount++;
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
			shift(size(), index, -1);
			end = Util.mod((end-1),vals.length);
		}
		else{
			shift(-1, index, 1);
			start = Util.mod((start+1),vals.length);
		}
		modCount++;
		return e;
	}

}
