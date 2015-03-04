package common.tuple;

/** A tuple of eight values, of types A, B, C, D, E, F, G and H, respectively */
public class Tuple8<A,B,C,D,E,F,G,H> extends AbsTuple {

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
		
		/** The sixth value stored within this tuple */
		public final F _6;
		
		/** The seventh value stored within this tuple */
		public final G _7;
		
		/** The eigth value stored within this tuple */
		public final H _8;
		
		/** Constructs a new tuple of the values (first, second, third, fourth, 
		 * fifth, sixth, seventh, eigth) */
		public Tuple8(A first, B second, C third, D fourth, E fifth, 
				F sixth, G seventh, H eigth){
			super(first, second, third, fourth, fifth, sixth, seventh, eigth);
			_1 = first;
			_2 = second;
			_3 = third;
			_4 = fourth;
			_5 = fifth;
			_6 = sixth;
			_7 = seventh;
			_8 = eigth;
		}
}
