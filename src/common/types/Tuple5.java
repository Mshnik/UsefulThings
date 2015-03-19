package common.types;

/** A tuple of five values, of types A, B, C, D and E, respectively */
public class Tuple5<A,B,C,D,E> extends AbsTuple {

		/** The first value stored within this tuple */
		public final A _1; 
		
		/** The second value stored within this tuple */
		public final B _2;
		
		/** The third value stored within this tuple */
		public final C _3;
		
		/** The fourth value stored within this tuple */
		public final D _4;
		
		/** The fifth value stored within this tuple */
		public final E _5;
		
		/** Constructs a new tuple of the values (first, second, third, fourth) */
		public Tuple5(A first, B second, C third, D fourth, E fifth){
			super(first, second, third, fourth, fifth);
			_1 = first;
			_2 = second;
			_3 = third;
			_4 = fourth;
			_5 = fifth;
		}
}
