package common.types;

/** A tuple of four values, of types A, B, C and D, respectively */
public class Tuple4<A,B,C,D> extends Tuple {

		/** The first value stored within this tuple */
		public final A _1; 
		
		/** The second value stored within this tuple */
		public final B _2;
		
		/** The third value stored within this tuple */
		public final C _3;
		
		/** The fourth value stored within this tuple */
		public final D _4;
		
		/** Constructs a new tuple of the values (first, second, third, fourth) */
		public Tuple4(A first, B second, C third, D fourth){
			super(first, second, third, fourth);
			_1 = first;
			_2 = second;
			_3 = third;
			_4 = fourth;
		}
}
