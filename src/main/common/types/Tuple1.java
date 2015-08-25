package common.types;

/** A tuple of one value, of types A */
public class Tuple1<A> extends Tuple {

	/** The first value stored within this tuple */
	public final A _1; 
	
	/** Constructs a new tuple of the value (first) */
	protected Tuple1(A first){
		super(first);
		_1 = first;
	}
}
