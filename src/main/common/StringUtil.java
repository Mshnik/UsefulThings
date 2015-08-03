package common;

import java.util.Iterator;

public class StringUtil {
	private StringUtil(){}
	
	public static CharIterator charIterator(String s){
		return new CharIterator(s);
	}
	
	static class CharIterator implements Iterator<Character> {
		public final String source;
		private final char[] sourceArr;
		private int index;
		
		public CharIterator(String source){
			this.source = source;
			sourceArr = source.toCharArray();
			index = 0;
		}
		
		@Override
		public boolean hasNext() {
			return index < sourceArr.length;
		}
	
		@Override
		public Character next() {
			return sourceArr[index++];
		}
	}
}
